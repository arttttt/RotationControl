package com.arttttt.rotationcontrolv3.ui.container.di

import com.arttttt.rotationcontrolv3.di.modules.FragmentFactoryModuleJava
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.container.ContainerFragment
import com.arttttt.rotationcontrolv3.ui.main2.di.MainComponentDependencies2
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
interface ContainerComponent : MainComponentDependencies2 {

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: ContainerDependencies
        ): ContainerComponent
    }

    fun inject(fragment: ContainerFragment)
}