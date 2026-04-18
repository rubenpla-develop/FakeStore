// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.kover)
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    apply(plugin = "org.jetbrains.kotlinx.kover")

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

tasks.register("koverTotalCoverage") {
    group = "verification"
    description = "Prints total line coverage aggregated from module Kover XML reports."
    dependsOn("koverXmlReport")

    doLast {
        val counterRegex =
            Regex("""<counter type="LINE" missed="(\d+)" covered="(\d+)"/>""")

        val reportFiles =
            fileTree(rootDir) {
                include("*/build/reports/kover/report.xml")
            }.files

        var missedTotal = 0L
        var coveredTotal = 0L

        reportFiles.forEach { file ->
            val match = counterRegex.find(file.readText()) ?: return@forEach
            missedTotal += match.groupValues[1].toLong()
            coveredTotal += match.groupValues[2].toLong()
        }

        val total = missedTotal + coveredTotal
        val pct = if (total > 0L) (coveredTotal * 100.0) / total else 0.0

        println(
            "Kover total line coverage: %.2f%% (covered=%d, missed=%d, total=%d)"
                .format(
                    pct,
                    coveredTotal,
                    missedTotal,
                    total,
                ),
        )
    }
}
