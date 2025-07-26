package delegates

import config.COMPILE_SDK_VERSION
import config.MIN_SDK_VERSION
import extensions.android
import extensions.libs
import extensions.pluginId
import org.gradle.api.Project

class LibraryDelegate : PluginDelegate {

    override fun apply(project: Project) {
        with(project) {
            pluginManager.apply(libs.pluginId("android-library"))

            android {
                compileSdk = COMPILE_SDK_VERSION

                defaultConfig {
                    minSdk = MIN_SDK_VERSION
                }
            }
        }
    }
}