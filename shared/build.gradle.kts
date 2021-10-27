import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization") version "1.5.31"
    id("com.codingfeline.buildkonfig")
}

val lifecycleVersion by extra("2.4.0-rc01")
val coroutinesVersion by extra("1.5.2-native-mt")
// TODO: move to stable - keeping eap to be able to build for M1 simulators
val ktorVersion by extra("2.0.0-eap-257")
val settingsVersion by extra("0.8.1")

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
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion") {
                    isForce = true
                }

                implementation("com.benasher44:uuid:0.3.1")

                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

                implementation("com.russhwolf:multiplatform-settings-coroutines-native-mt:$settingsVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                // TODO: might want to use JUnit with AssertJ instead (https://stackoverflow.com/a/63427057)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation("com.russhwolf:multiplatform-settings-test:$settingsVersion")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")

                implementation("io.ktor:ktor-client-android:$ktorVersion")

                implementation("androidx.datastore:datastore-preferences:1.0.0")
                implementation("androidx.startup:startup-runtime:1.1.0")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.13.2")
            }
        }
        val iosMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-ios:$ktorVersion")
            }
        }
        val iosTest by getting
    }
}

android {
    compileSdkVersion(31)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(23)
        targetSdkVersion(31)
    }
}

buildkonfig {
    packageName = "eu.rajniak.cat"

    defaultConfigs {
        buildConfigField(
            type = STRING,
            name = "api_key",
            value = findProperty("catsApiKey") as String
        )
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
    kotlinOptions.freeCompilerArgs += "-Xjvm-default=all"
}