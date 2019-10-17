buildscript {
    extra.set("kotlin_version", "1.3.50")

    repositories {
        google()
        jcenter()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:3.5.1")
        classpath(kotlin("gradle-plugin", extra.get("kotlin_version") as String))
    }
}

allprojects {
    dependencies {
        repositories {
            google()
            jcenter()
        }
    }
}

tasks {
    register<Delete>("clean") {
        delete(rootProject.buildDir)
    }
}