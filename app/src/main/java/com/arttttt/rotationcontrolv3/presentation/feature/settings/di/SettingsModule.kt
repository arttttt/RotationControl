package com.arttttt.rotationcontrolv3.presentation.feature.settings.di

import com.arttttt.rotationcontrolv3.presentation.feature.settings.pm.SettingsPM
import com.arttttt.rotationcontrolv3.presentation.feature.settings.view.SettingsFragment
import org.koin.core.qualifier.named
import org.koin.dsl.module

val settingsModule = module {
    scope(named<SettingsFragment>()) {
        scoped {
            SettingsPM(
                preferencesDelegate = get()
            )
        }
    }
}