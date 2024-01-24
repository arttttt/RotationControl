package com.arttttt.rotationcontrolv3.di

import android.content.Context
import com.arttttt.rotationcontrolv3.ui.container.di.ContainerDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent : ContainerDependencies {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}