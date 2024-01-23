plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":feature:permissions"))

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activityKtx)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.google.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)

    implementation(libs.androidx.multidex)

    implementation(libs.dagger.main)
    ksp(libs.dagger.compiler)

    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.logging)
    implementation(libs.mvikotlin.extensions.coroutines)

    implementation(libs.timber)
}