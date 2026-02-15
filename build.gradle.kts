// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.spotless) apply false
}

subprojects {
    apply(plugin = "com.diffplug.spotless")

    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        // Kotlin sources
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**", "**/generated/**")
            ktlint(libs.versions.ktlint.get())
        }

        // Kotlin Gradle scripts
        kotlinGradle {
            target("**/*.gradle.kts")
            ktlint(libs.versions.ktlint.get())
        }
    }
}