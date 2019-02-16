plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
}

android {
    compileSdkVersion(28)
    defaultConfig {
        minSdkVersion(15)
        targetSdkVersion(28)
    }
}

dependencies {
    implementation(Libs.kotlin_stdlib_jdk8)
    api(Libs.timber)
    api(Libs.sentry_android)
}
