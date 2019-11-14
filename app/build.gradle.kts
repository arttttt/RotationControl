plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.arttttt.rotationcontrolv3"
        versionCode = 9
        versionName = "1.2.1"

        minSdkVersion(16)
        targetSdkVersion(29)
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility= JavaVersion.VERSION_1_8
    }
}

dependencies {
    // Kotlin
    implementation(kotlin("stdlib-jdk8", rootProject.ext.get("kotlin_version") as String))

    // Support
    implementation("androidx.appcompat", "appcompat", "1.1.0")

    //Material
    implementation("com.google.android.material", "material", "1.0.0")

    // DI
    implementation("org.koin", "koin-core", "2.0.1")
    implementation("org.koin", "koin-android", "2.0.1")
    implementation("org.koin", "koin-android-scope", "2.0.1")

    // RxJava 2
    implementation("io.reactivex.rxjava2", "rxjava", "2.2.13")
    implementation("io.reactivex.rxjava2", "rxandroid", "2.1.1")
    implementation("io.reactivex.rxjava2", "rxkotlin", "2.4.0")
    implementation("com.jakewharton.rxbinding3", "rxbinding-core", "3.0.0")
    implementation("com.jakewharton.rxbinding3", "rxbinding-material", "3.0.0")

    // Presentation model pattern impl
    implementation("me.dmdev.rxpm", "rxpm", "2.0")

    // Navigation
    implementation ("ru.terrakok.cicerone" ,"cicerone", "5.0.0")

    // Timber
    implementation ("com.jakewharton.timber", "timber", "4.7.1")
}