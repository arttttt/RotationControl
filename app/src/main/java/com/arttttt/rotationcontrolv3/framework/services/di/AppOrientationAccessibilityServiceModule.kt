package com.arttttt.rotationcontrolv3.framework.services.di

import com.arttttt.rotationcontrolv3.di.scopes.PerService
import com.arttttt.rotationcontrolv3.domain.stores.apporientaton.AppOrientationStore
import com.arttttt.rotationcontrolv3.domain.stores.apporientaton.AppOrientationStoreFactory
import dagger.Module
import dagger.Provides

@Module
object AppOrientationAccessibilityServiceModule {

    @Provides
    @PerService
    fun provideAppOrientationStore(
        factory: AppOrientationStoreFactory
    ): AppOrientationStore {
        return factory.create()
    }
}