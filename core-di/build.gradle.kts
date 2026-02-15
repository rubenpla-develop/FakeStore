plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.rpla.fakestore.core.di"
    compileSdk = 36

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core-network"))
    implementation(project(":core-coroutines"))
    implementation(project(":core-database"))
    implementation(project(":core-data"))
    implementation(project(":core-domain"))

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
}
