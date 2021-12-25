plugins {
    id("com.diffplug.spotless") version "6.0.0"
}

buildscript {
    val kotlinVersion: String by project
    println("Using Kotlin Version: $kotlinVersion")

    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${kotlinVersion}")
        classpath("org.jetbrains.kotlin:kotlin-serialization:${kotlinVersion}")
        classpath("com.android.tools.build:gradle:7.0.4")
        classpath("com.codingfeline.buildkonfig:buildkonfig-gradle-plugin:0.11.0")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt")
            targetExclude("bin/**/*.kt")

            ktlint()
        }
    }
}
