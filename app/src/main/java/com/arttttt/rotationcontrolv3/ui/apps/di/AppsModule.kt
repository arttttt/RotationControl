package com.arttttt.rotationcontrolv3.ui.apps.di

import com.arttttt.rotationcontrolv3.data.repository.AppsRepositoryImpl
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import dagger.Binds
import dagger.Module

@Module
abstract class AppsModule {

    @Binds
    @PerScreen
    abstract fun bindAppsRepository(impl: AppsRepositoryImpl): AppsRepository
}