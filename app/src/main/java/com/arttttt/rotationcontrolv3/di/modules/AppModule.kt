package com.arttttt.rotationcontrolv3.di.modules

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
}