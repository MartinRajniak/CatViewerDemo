import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.plugin.mpp.Framework
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.codingfeline.buildkonfig")
}

kotlin {
    android()

    val iosTarget: (String, KotlinNativeTarget.() -> Unit) -> KotlinNativeTarget = when {
        System.getenv("SDK_NAME")?.startsWith("iphoneos") == true -> ::iosArm64
        System.getenv("NATIVE_ARCH")?.startsWith("arm") == true -> ::iosSimulatorArm64
        else -> ::iosX64
    }

    iosTarget("ios") {
        binaries {
            framework {
                baseName = "shared"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)

                implementation(libs.uuid)

                implementation(libs.ktor.client.core)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)

                implementation(libs.multiplatform.settings.coroutines)

                // Using api to export Kermit to iOS
                api(libs.kermit)
                api(libs.kermit.crashlytics)
            }
        }
        val commonTest by getting {
            dependencies {
                // TODO: might want to use JUnit with AssertJ instead (https://stackoverflow.com/a/63427057)
                implementation(libs.kotlin.test.common)
                implementation(libs.kotlin.test.annotations.common)

                implementation(libs.kotlinx.coroutines.test)

                implementation(libs.multiplatform.settings.test)

                implementation(libs.kermit.test)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.androidx.lifecycle.viewmodel)

                implementation(libs.ktor.client.android)

                implementation(libs.androidx.datastore.preferences)
                implementation(libs.androidx.startup.runtime)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(libs.kotlin.test.junit)
                implementation(libs.junit)
            }
        }
        val iosMain by getting {
            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }
        val iosTest by getting
    }

    // To make kermit available in iOS project - increases binary size because of the extra headers.
    // More info: https://github.com/touchlab/Kermit/blob/main/samples/sample-swift-export/README.md
    targets.withType<KotlinNativeTarget> {
        binaries.withType<Framework> {
            export(libs.kermit)
        }
    }
}

android {
    compileSdkVersion(31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(30)
    }
}

buildkonfig {
    packageName = "eu.rajniak.cat"

    defaultConfigs {
        buildConfigField(
            type = STRING,
            name = "api_key",
            value = findProperty("catsApiKey") as? String ?: throw Exception("catsApiKey is not set")
        )
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
}
