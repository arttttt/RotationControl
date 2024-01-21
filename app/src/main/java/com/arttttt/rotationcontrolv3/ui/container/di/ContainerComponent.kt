package com.arttttt.rotationcontrolv3.ui.container.di

import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.container.ContainerFragment
import com.github.terrakok.cicerone.Router
import dagger.BindsInstance
import dagger.Component

@PerScreen
@Component
interface ContainerComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance router: Router
        ): ContainerComponent
    }

    fun inject(fragment: ContainerFragment)
}