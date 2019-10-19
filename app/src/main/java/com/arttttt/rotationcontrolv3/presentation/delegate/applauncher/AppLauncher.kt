package com.arttttt.rotationcontrolv3.presentation.delegate.applauncher

import com.arttttt.rotationcontrolv3.Screens
import ru.terrakok.cicerone.Screen

class AppLauncher: IAppLauncher {
    override val rootScreen: Screen = Screens.MainFlowScreen
}