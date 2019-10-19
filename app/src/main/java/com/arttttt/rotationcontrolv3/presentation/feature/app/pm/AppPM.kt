package com.arttttt.rotationcontrolv3.presentation.feature.app.pm

import com.arttttt.rotationcontrolv3.presentation.base.BasePresentationModel
import com.arttttt.rotationcontrolv3.presentation.delegate.applauncher.IAppLauncher

class AppPM(
    private val appLauncher: IAppLauncher
): BasePresentationModel() {

    override fun onCreate() {
        super.onCreate()

        router.newRootScreen(appLauncher.rootScreen)
    }

    override fun backPressed() {
        router.exit()
    }
}