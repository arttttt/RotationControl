plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.arttttt.utils"

    compileSdk = 34

    defaultConfig {
        minSdk = 15
    }

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

    kotlinOptions {
        jvmTarget = "17"

        freeCompilerArgs = listOf("-Xcontext-receivers")
    }
}

dependencies {
    implementation(libs.kotlin.coroutines.core)

    implementation(libs.timber)
}