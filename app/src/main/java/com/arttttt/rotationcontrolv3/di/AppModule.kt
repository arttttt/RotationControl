package com.arttttt.rotationcontrolv3.di

import com.arttttt.rotationcontrolv3.presentation.feature.about.di.aboutModule
import com.arttttt.rotationcontrolv3.presentation.feature.hostModule
import com.arttttt.rotationcontrolv3.presentation.feature.settings.di.settingsModule

val appModules = listOf(
    settingsModule,
    commonModule,
    aboutModule,
    hostModule
)