@file:Suppress("NewApi")

package extensions

import org.gradle.api.artifacts.ExternalModuleDependencyBundle
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionConstraint
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.plugin.use.PluginDependency

internal fun VersionCatalog.library(name: String): Provider<MinimalExternalModuleDependency> {
    val library = findLibrary(name)
    if (!library.isPresent) {
        error("Library \"$name\" not found. Check your *.versions.toml for existence of \"$name\" in [libraries] section")
    }
    return library.get()
}

internal fun VersionCatalog.version(name: String): VersionConstraint {
    val version = findVersion(name)
    if (!version.isPresent) {
        error("Version \"$name\" not found. Check your *.versions.toml for existence of \"$name\" in [versions] section")
    }
    return version.get()
}

internal fun VersionCatalog.plugin(name: String): PluginDependency {
    val plugin = findPlugin(name)
    if (!plugin.isPresent) {
        error("Plugin \"$name\" not found. Check your *.versions.toml for existence of \"$name\" in [plugins] section")
    }
    return plugin.get().get()
}

internal fun VersionCatalog.pluginId(name: String): String {
    val plugin = findPlugin(name)
    if (!plugin.isPresent) {
        error("Plugin \"$name\" not found. Check your *.versions.toml for existence of \"$name\" in [plugins] section")
    }
    return plugin.get().get().pluginId
}

internal fun VersionCatalog.bundle(name: String): Provider<ExternalModuleDependencyBundle> {
    val bundle = findBundle(name)
    if (!bundle.isPresent) {
        error("Bundle \"$name\" not found. Check your *.versions.toml for existence of \"$name\" in [bundles] section")
    }
    return bundle.get()
}

internal fun DependencyHandlerScope.implementation(lib: Any) {
    add("implementation", lib)
}

internal fun DependencyHandlerScope.debugImplementation(lib: Any) {
    add("debugImplementation", lib)
}

internal fun DependencyHandlerScope.androidTestImplementation(lib: Any) {
    add("androidTestImplementation", lib)
}

internal fun DependencyHandlerScope.ksp(plugin: Any) {
    add("ksp", plugin)
}