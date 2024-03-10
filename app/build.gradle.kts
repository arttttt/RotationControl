plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.arttttt.rotationcontrolv3"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.arttttt.rotationcontrolv3"
        minSdk = 16
        targetSdk = 34
        versionCode = 11
        versionName = "2.0.1"

        multiDexEnabled = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"

            resValue("string", "app_name", "@string/app_name_dev")
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            resValue("string", "app_name", "@string/app_name_release")
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

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":core:navigation"))
    implementation(project(":core:permissions"))
    implementation(project(":utils"))

    implementation(libs.androidx.coreKtx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activityKtx)
    implementation(libs.androidx.fragmentKtx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.lifecycle.runtimeKtx)
    implementation(libs.google.material)

    implementation(libs.androidx.multidex)

    implementation(libs.dagger.main)
    ksp(libs.dagger.compiler)

    implementation(libs.mvikotlin.core)
    implementation(libs.mvikotlin.main)
    implementation(libs.mvikotlin.logging)
    implementation(libs.mvikotlin.extensions.coroutines)
    implementation(libs.essenty.lifecycle)
    implementation(libs.essenty.stateKeeper)
    implementation(libs.essenty.instanceKeeper)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlyticsKtx)
    implementation(libs.firebase.analyticsKtx)

    implementation(libs.timber)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
}