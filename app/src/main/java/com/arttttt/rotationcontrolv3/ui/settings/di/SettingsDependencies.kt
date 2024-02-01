package com.arttttt.rotationcontrolv3.ui.settings.di

import android.content.Context
import com.arttttt.rotationcontrolv3.domain.stores.SettingsStore
import com.arttttt.rotationcontrolv3.utils.resources.ResourcesProvider

interface SettingsDependencies {

    val context: Context
    val resourcesProvider: ResourcesProvider
    val settingsStore: SettingsStore
}