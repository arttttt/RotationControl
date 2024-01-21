package com.arttttt.rotationcontrolv3.ui.main.di

import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.main.MainFragment
import com.arttttt.navigation.FlowMenuRouter
import dagger.BindsInstance
import dagger.Component

@PerScreen
@Component
interface MainComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance router: FlowMenuRouter
        ): MainComponent
    }

    fun inject(fragment: MainFragment)
}