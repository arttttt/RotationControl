package com.arttttt.rotationcontrolv3.di.components

import android.content.Context
import com.arttttt.rotationcontrolv3.di.modules.SingletonComponent
import com.arttttt.rotationcontrolv3.ui.container.di.ContainerDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        SingletonComponent::class,
    ]
)
interface AppComponent : ContainerDependencies {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}