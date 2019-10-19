package com.arttttt.rotationcontrolv3.utils.navigation

import com.arttttt.rotationcontrolv3.presentation.base.FlowAppScreen
import ru.terrakok.cicerone.Router

class FlowRouter(private val appRouter: Router) : Router() {

    fun startFlow(screen: FlowAppScreen) {
        appRouter.navigateTo(screen)
    }

    fun newRootFlow(screen: FlowAppScreen) {
        appRouter.newRootScreen(screen)
    }

    fun finishFlow() {
        appRouter.exit()
    }
}
