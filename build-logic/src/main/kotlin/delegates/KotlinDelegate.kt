package delegates

import extensions.android
import extensions.kotlinOptions
import extensions.libs
import extensions.pluginId
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

class KotlinDelegate : PluginDelegate {

    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply(libs.pluginId("jetbrains-kotlin-android"))

            android {
                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                kotlinOptions {
                    jvmTarget.set(JvmTarget.JVM_17)
                }
            }
        }
    }
}