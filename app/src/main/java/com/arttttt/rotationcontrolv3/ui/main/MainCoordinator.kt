package com.arttttt.rotationcontrolv3.ui.main

import com.arttttt.rotationcontrolv3.ui.Screens
import com.arttttt.rotationcontrolv3.utils.navigation.MenuRouter
import javax.inject.Inject

class MainCoordinator @Inject constructor(
    private val router: MenuRouter,
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