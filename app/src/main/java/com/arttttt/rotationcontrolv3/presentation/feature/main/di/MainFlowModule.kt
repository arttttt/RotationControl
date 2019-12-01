package com.arttttt.rotationcontrolv3.presentation.feature.main.di

import com.arttttt.rotationcontrolv3.presentation.feature.main.pm.MainFlowPM
import com.arttttt.rotationcontrolv3.presentation.feature.main.view.MainFragment
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainFlowModule = module {
    scope(named<MainFragment>()) {
        scoped {
            MainFlowPM(
                rotationServiceHelper = get(),
                canWriteSettingsChecker = get(),
                canWriteSettingsRequester = get(),
                preferencesDelegate = get(),
                canDrawOverlayChecker = get(),
                canDrawOverlayRequester = get()
            )
        }
    }
}