package delegates

import org.gradle.api.Project

interface PluginDelegate {

    fun apply(project: Project)
}