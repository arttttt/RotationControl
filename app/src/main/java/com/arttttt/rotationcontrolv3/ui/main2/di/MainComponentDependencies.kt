package com.arttttt.rotationcontrolv3.ui.main2.di

import android.content.Context
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.rotationcontrolv3.di.qualifiers.RootRouterQualifier
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import com.arttttt.rotationcontrolv3.domain.stores.settings.SettingsStore
import com.arttttt.rotationcontrolv3.utils.resources.ResourcesProvider

interface MainComponentDependencies2 {

    @get:RootRouterQualifier
    val rootRouter: FlowMenuRouter

    val context: Context
    val resourcesProvider: ResourcesProvider
    val settingsStore: SettingsStore
    val permissionsRepository: PermissionsRepository
    val settingsRepository: SettingsRepository
    val storeFactory: StoreFactory
    val appsRepository: AppsRepository
}