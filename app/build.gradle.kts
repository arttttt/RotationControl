plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("com.google.devtools.ksp")
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

        multiDexEnabled = true
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
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation(libs.material)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation("androidx.multidex:multidex:2.0.1")

    implementation("com.google.dagger:dagger:2.50")
    ksp("com.google.dagger:dagger-compiler:2.50")

    implementation("com.arkivanov.mvikotlin:mvikotlin:4.0.0-alpha02")
    implementation("com.arkivanov.mvikotlin:mvikotlin-main:4.0.0-alpha02")
    implementation("com.arkivanov.mvikotlin:mvikotlin-logging:4.0.0-alpha02")
    implementation("com.arkivanov.mvikotlin:mvikotlin-extensions-coroutines:4.0.0-alpha02")

    implementation("com.github.terrakok:cicerone:7.1")

    implementation("com.jakewharton.timber:timber:5.0.1")
}