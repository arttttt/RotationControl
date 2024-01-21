package com.arttttt.rotationcontrolv3.ui.container

import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.main.MainFragment
import com.arttttt.rotationcontrolv3.utils.extensions.instantiate
import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen
import javax.inject.Inject

@PerScreen
class ContainerCoordinator @Inject constructor(
    private val router: Router
) {

    fun start() {
        router.newRootScreen(
            FragmentScreen { factory ->
                factory.instantiate<MainFragment>()
            }
        )
    }
}