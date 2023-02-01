package com.javierscs.conventional.commits.gradle.plugin

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class CommitTests {

    @Test
    fun `given a commit content with feat, no scope, body + breaking change, and two footers, when creeting a Commit then it converted to string is equal to the original content`() {
        val expect =
            """ |feat: allow provided config object to extend other configs
                |
                |BREAKING CHANGE: `extends` key in config file is now used for extending other config files
                |
                |footer 1 `extends` key in config file is now used for extending other config files
                |
                |footer 2 `extends` key in config file is now used for extending other config files
            """
                .trimMargin()
        val actual = "${Commit("1234567", expect)}"

        actual.shouldBe(expect)
    }
}
