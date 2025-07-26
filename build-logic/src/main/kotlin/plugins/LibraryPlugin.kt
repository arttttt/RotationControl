package plugins

import delegates.LibraryDelegate
import org.gradle.api.Plugin
import org.gradle.api.Project

class LibraryPlugin : Plugin<Project> {

    private val delegates = listOf(
        LibraryDelegate()
    )

    override fun apply(target: Project) {
        delegates.forEach { delegate -> delegate.apply(target) }
    }
}