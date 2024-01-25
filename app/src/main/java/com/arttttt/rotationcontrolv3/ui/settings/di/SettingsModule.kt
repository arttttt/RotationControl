package com.arttttt.rotationcontrolv3.ui.settings.di

import com.arttttt.rotationcontrolv3.data.repository.SettingsRepositoryImpl
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class SettingsModule {

    @Binds
    @PerScreen
    abstract fun bindsSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}