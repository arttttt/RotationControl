package delegates

import config.COMPILE_SDK_VERSION
import config.MIN_SDK_VERSION
import extensions.application
import extensions.libs
import extensions.pluginId
import org.gradle.api.Project

class ApplicationDelegate : PluginDelegate {

    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply(libs.pluginId("android-application"))

            application {
                compileSdk = COMPILE_SDK_VERSION

                defaultConfig {
                    minSdk = MIN_SDK_VERSION
                    targetSdk = COMPILE_SDK_VERSION
                }
            }
        }
    }
}