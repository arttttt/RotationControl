plugins {
    id("library")
}

android {
    namespace = "com.arttttt.adapterdelegates"
}

dependencies {
    implementation(project(":utils"))

    implementation(libs.androidx.recyclerview)
}