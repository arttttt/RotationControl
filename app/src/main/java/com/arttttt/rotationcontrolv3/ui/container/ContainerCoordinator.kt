package com.arttttt.rotationcontrolv3.ui.container

import com.arttttt.rotationcontrolv3.di.qualifiers.RootCiceroneQualifier
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.Screens
import com.github.terrakok.cicerone.Router
import javax.inject.Inject

@PerScreen
class ContainerCoordinator @Inject constructor(
    @RootCiceroneQualifier private val router: Router
) {

    fun start() {
        router.newRootScreen(Screens.MainScreen())
    }
}