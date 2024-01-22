package com.arttttt.rotationcontrolv3.ui.container.di

import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.rotationcontrolv3.di.FragmentFactoryModuleJava
import com.arttttt.rotationcontrolv3.di.qualifiers.RootRouterQualifier
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.container.ContainerFragment
import com.arttttt.rotationcontrolv3.ui.main.di.MainComponentDependencies
import dagger.BindsInstance
import dagger.Component

@PerScreen
@Component(
    modules = [
        ContainerModule::class,
        FragmentFactoryModuleJava::class,
    ]
)
interface ContainerComponent : MainComponentDependencies {

    @Component.Factory
    interface Factory {

        fun create(): ContainerComponent
    }

    fun inject(fragment: ContainerFragment)
}