package com.arttttt.navigation

import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.Screen
import com.github.terrakok.cicerone.androidx.FragmentScreen

class FlowMenuRouter(
    private val parentRouter: FlowMenuRouter?
) : Router() {

    fun showScreen(screen: FragmentScreen) {
        executeCommands(Show(screen))
    }

    fun newRootFlow(screen: Screen) {
        parentRouter?.newRootScreen(screen)
    }

    fun newRootFlowChain(vararg screens: Screen) {
        parentRouter?.newRootChain(*screens)
    }
}