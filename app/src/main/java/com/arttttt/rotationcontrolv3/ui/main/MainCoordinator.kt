package com.arttttt.rotationcontrolv3.ui.main

import com.arttttt.rotationcontrolv3.ui.Screens
import com.arttttt.navigation.FlowMenuRouter
import javax.inject.Inject

class MainCoordinator @Inject constructor(
    private val router: FlowMenuRouter,
) {

    fun start() {
        router.showScreen(Screens.SettingsScreen())
    }

    fun showSettings() {
        router.showScreen(Screens.SettingsScreen())
    }

    fun showAbout() {
        router.showScreen(Screens.AboutScreen())
    }
}