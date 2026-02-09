pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

    plugins {
        id("com.android.application") version "9.0.0"
        id("com.android.library") version "9.0.0"

        id("org.jetbrains.kotlin.android") version "2.0.21"
        id("org.jetbrains.kotlin.plugin.compose") version "2.0.21"

        id("org.jetbrains.kotlin.kapt") version "2.0.21"

        id("com.google.dagger.hilt.android") version "2.55"
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "FakeStore"
include(":app")
include(":core-ui")
include(":core-domain")
include(":core-model")
include(":core-network")
include(":core-database")
include(":core-data")

include(":core-di")

include(":feature-products")
//include(":feature-favorites")
//include(":feature-profile")
