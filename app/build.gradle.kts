plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.arttttt.rotationcontrolv3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arttttt.rotationcontrolv3"
        minSdk = 15
        targetSdk = 34
        versionCode = 10
        versionName = "2.0.0"
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
        sourceCompatibility = JavaVersion.VERSION_19
        targetCompatibility = JavaVersion.VERSION_19
    }
    kotlinOptions {
        jvmTarget = "19"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation("com.arkivanov.mvikotlin:mvikotlin:4.0.0-alpha02")
    implementation("com.arkivanov.mvikotlin:mvikotlin-main:4.0.0-alpha02")
    implementation("com.arkivanov.mvikotlin:mvikotlin-logging:4.0.0-alpha02")
    implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:4.0.0-alpha02")
}