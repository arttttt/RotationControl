package plugins

import delegates.KotlinDelegate
import delegates.LibraryDelegate
import org.gradle.api.Plugin
import org.gradle.api.Project

class LibraryPlugin : Plugin<Project> {

    private val delegates = listOf(
        LibraryDelegate(),
        KotlinDelegate(),
    )

    override fun apply(target: Project) {
        delegates.forEach { delegate -> delegate.apply(target) }
    }
}