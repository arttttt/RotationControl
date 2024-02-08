package com.arttttt.rotationcontrolv3.ui.rotation.di

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arttttt.rotationcontrolv3.di.scopes.PerService
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService
import dagger.BindsInstance
import dagger.Component

@PerService
@Component(
    dependencies = [
        RotationServiceDependencies::class,
    ],
    modules = [
        RotationServiceModule::class,
    ]
)
interface RotationServiceComponent {

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: RotationServiceDependencies,
            @BindsInstance instanceKeeper: InstanceKeeper,
        ) : RotationServiceComponent
    }

    fun inject(service: RotationService)
}