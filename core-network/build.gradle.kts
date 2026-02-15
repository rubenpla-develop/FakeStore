plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.rpla.fakestore.core.network"
    compileSdk = 36
    defaultConfig { minSdk = 26 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation(project(":core-domain"))
    implementation(libs.javax.inject)

    api(libs.retrofit)
    api(libs.retrofit.converter.moshi)

    api(libs.okhttp)
    api(libs.okhttp.logging.interceptor)

    api(libs.moshi)
    api(libs.moshi.kotlin)
}
