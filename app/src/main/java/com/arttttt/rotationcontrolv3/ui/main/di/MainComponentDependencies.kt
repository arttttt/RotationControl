package com.arttttt.rotationcontrolv3.ui.main.di

import android.content.Context
import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.rotationcontrolv3.di.qualifiers.RootRouterQualifier
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.domain.stores.SettingsStore
import com.arttttt.rotationcontrolv3.utils.resources.ResourcesProvider

interface MainComponentDependencies {

    @get:RootRouterQualifier
    val rootRouter: FlowMenuRouter

    val context: Context
    val resourcesProvider: ResourcesProvider
    val settingsStore: SettingsStore
    val permissionsRepository: PermissionsRepository
    val settingsRepository: SettingsRepository
}