package com.arttttt.rotationcontrolv3.ui.container.di

import com.arttttt.rotationcontrolv3.di.FragmentFactoryModuleJava
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.container.ContainerFragment
import com.arttttt.rotationcontrolv3.ui.main.di.MainComponentDependencies
import dagger.Component

@PerScreen
@Component(
    dependencies = [
        ContainerDependencies::class
    ],
    modules = [
        ContainerModule::class,
        FragmentFactoryModuleJava::class,
    ]
)
interface ContainerComponent : MainComponentDependencies {

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: ContainerDependencies
        ): ContainerComponent
    }

    fun inject(fragment: ContainerFragment)
}