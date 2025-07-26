import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("library")
}

android {
    namespace = "com.arttttt.permissions"

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.add("-Xcontext-receivers")
        }
    }
}

dependencies {
    implementation(project(":utils"))

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activityKtx)
    implementation(libs.androidx.lifecycle.runtimeKtx)

    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.extensions.coroutines)

    implementation(libs.timber)
}