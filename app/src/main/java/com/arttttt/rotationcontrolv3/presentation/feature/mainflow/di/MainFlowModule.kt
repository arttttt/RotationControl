package com.arttttt.rotationcontrolv3.presentation.feature.mainflow.di

import com.arttttt.rotationcontrolv3.presentation.feature.mainflow.pm.MainFlowPM
import com.arttttt.rotationcontrolv3.presentation.feature.mainflow.view.MainFlowFragment
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainFlowModule = module {
    scope(named<MainFlowFragment>()) {
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