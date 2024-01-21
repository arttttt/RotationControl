package com.arttttt.rotationcontrolv3.utils.navigation

import com.github.terrakok.cicerone.Router
import com.github.terrakok.cicerone.androidx.FragmentScreen

class MenuRouter : Router() {

    fun showScreen(screen: FragmentScreen) {
        executeCommands(Show(screen))
    }
}