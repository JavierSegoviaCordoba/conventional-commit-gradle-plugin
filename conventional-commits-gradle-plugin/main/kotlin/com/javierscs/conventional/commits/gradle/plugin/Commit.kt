package com.javierscs.conventional.commits.gradle.plugin

import com.javiersc.kotlin.stdlib.secondOrNull

data class Commit(val hash: String, val content: String) {

    private val lines = content.lines()
    private val count = lines.count()
    private val message: String = require(lines.firstOrNull() != null).run { lines.first() }
    private val typeAndScope = message.substringBefore(": ")
    private val bodyStartIndex = 2
    private val bodyEndIndex =
        bodyStartIndex + lines.drop(bodyStartIndex).takeWhile(String::isNotBlank).count()
    private val footerStartIndex = bodyEndIndex + 1

    init {
        require(message.contains(": "))
        if (typeAndScope.contains("(")) require(typeAndScope.contains(")"))
        if (typeAndScope.contains(")")) require(typeAndScope.contains("("))
        if (count > 1) require(lines.secondOrNull()?.isBlank())
    }

    val type: Type = run {
        val uncheckedType = typeAndScope.substringBefore("<")
        Type.values().first { it.description == uncheckedType }
    }

    val scope: String? =
        if (typeAndScope.contains("(") && typeAndScope.contains(")")) {
            typeAndScope.substringAfter("(").substringBefore(")")
        } else {
            null
        }

    val description: String = message.substringAfter(": ")

    val body: String? =
        if (count > 1) lines.subList(bodyStartIndex, bodyEndIndex).joinToString("\n") else null

    val footers: List<String> =
        if (body != null && count >= footerStartIndex) lines.subList(footerStartIndex, count)
        else emptyList()

    private val isBreakingChangeOnMessage = typeAndScope.contains("!")

    private val isBreakingChangeOnBody = body?.startsWith("BREAKING CHANGE: ") ?: false

    private val isBreakingChangeOnFooters: Boolean =
        footers.any { it.startsWith("BREAKING CHANGE: ") }

    val isBreakingChange: Boolean =
        isBreakingChangeOnMessage || isBreakingChangeOnBody || isBreakingChangeOnFooters

    override fun toString(): String = buildString {
        append("$type")
        if (scope != null) append("($scope)")
        if (isBreakingChangeOnMessage) append("!")
        append(": ")
        append(description)
        if (body != null) {
            appendLine("\n")
            append(body)
        }
        if (footers.isNotEmpty()) {
            appendLine("\n")
            append(footers.joinToString("\n"))
        }
    }

    enum class Type(val description: String) {
        Build("build"),
        Chore("chore"),
        CI("ci"),
        Docs("docs"),
        Feat("feat"),
        Fix("fix"),
        Perf("perf"),
        Refactor("refactor"),
        Revert("revert"),
        Style("perf"),
        Test("style"),
        ;
        override fun toString() = description
    }
}

internal fun Commit.require(value: Boolean?): Commit = if (value == true) this else invalidCommit()

internal fun Commit.invalidCommit(): Nothing =
    error("The commit($hash) is not following all conventional rules")
