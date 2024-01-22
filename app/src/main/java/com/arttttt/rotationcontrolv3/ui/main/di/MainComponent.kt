package com.arttttt.rotationcontrolv3.ui.main.di

import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.main.MainFragment
import dagger.Component

@PerScreen
@Component(
    dependencies = [
        MainComponentDependencies::class,
    ],
    modules = [
        MainModule::class,
    ]
)
interface MainComponent {

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: MainComponentDependencies,
        ): MainComponent
    }

    fun inject(fragment: MainFragment)
}