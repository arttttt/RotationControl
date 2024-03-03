package com.arttttt.rotationcontrolv3.ui.container

import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.rotationcontrolv3.di.qualifiers.RootRouterQualifier
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.ui.Screens
import javax.inject.Inject

@PerScreen
class ContainerCoordinator @Inject constructor(
    @RootRouterQualifier private val router: FlowMenuRouter
) {

    fun start() {
        router.newRootScreen(Screens.MainScreen2())
    }
}