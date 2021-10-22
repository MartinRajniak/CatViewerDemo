plugins {
    id("com.android.application")
    kotlin("android")
}

val composeVersion by extra ("1.0.4")

dependencies {
    implementation(project(":shared"))

    // Compose
    implementation("androidx.activity:activity-compose:1.3.1")

    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.foundation:foundation:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-core:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")

    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:$composeVersion")
}

android {
    compileSdkVersion(31)
    defaultConfig {
        applicationId = "eu.rajniak.cat.android"
        minSdkVersion(23)
        targetSdkVersion(31)
        versionCode = 1
        versionName = "1.0"
    }
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}