package com.arttttt.rotationcontrolv3.ui.settings.di

import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.settings.platform.SettingsFragment
import dagger.Component

@PerScreen
@Component(
    dependencies = [
        SettingsDependencies::class,
    ],
    modules = [
        SettingsModule::class,
    ]
)
interface SettingsComponent {

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: SettingsDependencies,
        ) : SettingsComponent
    }

    fun inject(fragment: SettingsFragment)
}