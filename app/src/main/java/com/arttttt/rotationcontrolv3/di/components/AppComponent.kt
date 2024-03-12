package com.arttttt.rotationcontrolv3.di.components

import android.content.Context
import com.arttttt.rotationcontrolv3.di.modules.AppModule
import com.arttttt.rotationcontrolv3.framework.services.di.AppOrientationAccessibilityServiceDependencies
import com.arttttt.rotationcontrolv3.ui.container.di.ContainerDependencies
import com.arttttt.rotationcontrolv3.ui.rotation.di.RotationServiceDependencies
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
    ]
)
interface AppComponent : ContainerDependencies,
    RotationServiceDependencies,
    AppOrientationAccessibilityServiceDependencies {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context,
        ): AppComponent
    }
}