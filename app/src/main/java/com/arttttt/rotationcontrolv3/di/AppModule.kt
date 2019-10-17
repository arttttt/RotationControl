package com.arttttt.rotationcontrolv3.di

import com.arttttt.rotationcontrolv3.presentation.feature.about.di.aboutModule
import com.arttttt.rotationcontrolv3.presentation.feature.app.di.appModule
import com.arttttt.rotationcontrolv3.presentation.feature.mainflow.di.mainFlowModule
import com.arttttt.rotationcontrolv3.presentation.feature.settings.di.settingsModule

val appModules = listOf(
    settingsModule,
    commonModule,
    aboutModule,
    appModule,
    mainFlowModule
)