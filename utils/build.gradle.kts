import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("library")
}

android {
    namespace = "com.arttttt.utils"

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
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activityKtx)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.recyclerview)

    implementation(libs.kotlin.coroutines.core)

    implementation(libs.timber)
}