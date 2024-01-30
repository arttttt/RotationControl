package com.arttttt.rotationcontrolv3.di.modules

import com.arttttt.rotationcontrolv3.data.repository.SensorsRepositoryImpl
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.utils.resources.ResourceProviderImpl
import com.arttttt.rotationcontrolv3.utils.resources.ResourcesProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindResourcesProvider(impl: ResourceProviderImpl): ResourcesProvider

    @Binds
    @Singleton
    abstract fun bindSensorsRepository(impl: SensorsRepositoryImpl): SensorsRepository
}