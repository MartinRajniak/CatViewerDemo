import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.spotless)
    alias(libs.plugins.versions)
    alias(libs.plugins.version.catalog.update)
}

buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
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

fun isNonStable(version: String) = listOf(
    "alpha", "beta", // Firebase, Avast
    "-M" // JUnit Jupiter
).any { version.contains(it) }

// Add here dependencies that cannot be updated at the moment
fun cannotUpdate(module: String) = emptyList<String>().any { module.contains(it) }

tasks.named<DependencyUpdatesTask>("dependencyUpdates").configure {
    resolutionStrategy {
        componentSelection {
            all {
                if (cannotUpdate(candidate.module)) {
                    reject("Ignore because we cannot update at the moment.")
                }
                if (isNonStable(candidate.version) && !isNonStable(currentVersion)) {
                    reject("Ignore because version is not stable")
                }
            }
        }
    }
}