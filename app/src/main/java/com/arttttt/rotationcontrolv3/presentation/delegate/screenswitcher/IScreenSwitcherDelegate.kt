package com.arttttt.rotationcontrolv3.presentation.delegate.screenswitcher

import ru.terrakok.cicerone.android.support.SupportAppScreen

interface IScreenSwitcherDelegate {
    fun switchScreen(screenToShow: SupportAppScreen)
}