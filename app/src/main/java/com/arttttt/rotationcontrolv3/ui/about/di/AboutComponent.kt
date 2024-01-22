package com.arttttt.rotationcontrolv3.ui.about.di

import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.about.AboutFragment
import dagger.Component

@PerScreen
@Component(
    dependencies = [
        AboutComponentDependencies::class,
    ]
)
interface AboutComponent {

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: AboutComponentDependencies
        ): AboutComponent
    }

    fun inject(fragment: AboutFragment)
}