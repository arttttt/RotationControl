package extensions

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile

private const val NO_ANDROID_EXTENSION = "android extension not found"

internal fun Project.android(
    block: CommonExtension<*, *, *, *, *, *>.() -> Unit,
) {
    val android = extensions.findByName("android") as? CommonExtension<*, *, *, *, *, *> ?: error(NO_ANDROID_EXTENSION)

    android.block()
}

internal fun Project.application(
    block: ApplicationExtension.() -> Unit,
) {
    val android = extensions.findByName("android") as? ApplicationExtension ?: error(NO_ANDROID_EXTENSION)

    android.block()
}

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.kotlinOptions(
    block: KotlinJvmCompilerOptions.() -> Unit,
) {
    tasks.withType<KotlinJvmCompile>().configureEach {
        compilerOptions(block)
    }
}