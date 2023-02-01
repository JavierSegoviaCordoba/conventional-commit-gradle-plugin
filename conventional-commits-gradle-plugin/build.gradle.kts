plugins {
    alias(libs.plugins.javiersc.hubdle)
}

hubdle {
    kotlin {
        jvm {
            features {
                gradle {
                    plugin {
                        tags(
                            "conventional",
                            "commit",
                            "commits",
                            "conventional commits",
                        )

                        gradlePlugin {
                            plugins {
                                create("ConventionalCommitsPlugins") {
                                    id = "com.javiersc.semver.gradle.plugin"
                                    displayName = "Conventional Commits"
                                    description = "Check if commits are conventional"
                                    implementationClass =
                                        "com.javiersc.conventional.commits.gradle.plugin.ConventionalCommitsPlugin"
                                }
                            }
                        }

                        pluginUnderTestDependencies(
                            androidToolsBuildGradle(),
                            jetbrainsKotlinGradlePlugin(),
                        )
                    }
                }
            }
        }
    }
}
