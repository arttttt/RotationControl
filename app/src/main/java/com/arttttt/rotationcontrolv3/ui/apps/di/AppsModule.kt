package com.arttttt.rotationcontrolv3.ui.apps.di

import com.arttttt.rotationcontrolv3.data.repository.AppsRepositoryImpl
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStoreFactory
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class AppsModule {

    companion object {

        @Provides
        @PerScreen
        fun provideAppsStore(
            factory: AppsStoreFactory,
        ): AppsStore {
            return factory.create()
        }
    }

    @Binds
    @PerScreen
    abstract fun bindAppsRepository(impl: AppsRepositoryImpl): AppsRepository
}