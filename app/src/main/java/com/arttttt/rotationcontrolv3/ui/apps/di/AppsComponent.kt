package com.arttttt.rotationcontrolv3.ui.apps.di

import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.apps.AppsFragment
import dagger.Component

@PerScreen
@Component(
    dependencies = [
        AppsComponentDependencies::class,
    ],
    modules = [
        AppsModule::class,
    ],
)
interface AppsComponent {

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: AppsComponentDependencies
        ) : AppsComponent
    }

    fun inject(fragment: AppsFragment)
}