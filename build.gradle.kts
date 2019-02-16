// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath(Libs.com_android_tools_build_gradle)
        classpath(Libs.kotlin_gradle_plugin)
    }
}

plugins {
    id("com.github.dcendents.android-maven") version Versions.com_github_dcendents_android_maven_gradle_plugin
    id("de.fayard.buildSrcVersions") version Versions.de_fayard_buildsrcversions_gradle_plugin
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.wrapper {
    version = Versions.Gradle.runningVersion
    distributionType = Wrapper.DistributionType.BIN
}

