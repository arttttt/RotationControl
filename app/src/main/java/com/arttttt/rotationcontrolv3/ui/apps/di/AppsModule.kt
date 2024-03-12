package com.arttttt.rotationcontrolv3.ui.apps.di

import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStoreFactory
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
}