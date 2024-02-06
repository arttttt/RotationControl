package com.arttttt.rotationcontrolv3.ui.container.di

import android.content.Context
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.domain.stores.settings.SettingsStore
import com.arttttt.rotationcontrolv3.utils.resources.ResourcesProvider

interface ContainerDependencies {

    val context: Context
    val resourcesProvider: ResourcesProvider
    val settingsStore: SettingsStore
    val permissionsRepository: PermissionsRepository
    val settingsRepository: SettingsRepository
}