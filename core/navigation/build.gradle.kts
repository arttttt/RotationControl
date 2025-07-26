import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("library")
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.arttttt.navigation"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17

            freeCompilerArgs.add("-Xcontext-receivers")
        }
    }
}

dependencies {
    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activityKtx)
    implementation(libs.androidx.fragmentKtx)

    api(libs.cicerone)

    implementation(libs.timber)
}