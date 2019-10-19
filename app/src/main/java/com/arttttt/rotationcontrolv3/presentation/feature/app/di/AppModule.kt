package com.arttttt.rotationcontrolv3.presentation.feature.app.di

import com.arttttt.rotationcontrolv3.presentation.feature.app.pm.AppPM
import com.arttttt.rotationcontrolv3.presentation.feature.app.view.AppActivity
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    scope(named<AppActivity>()) {
        scoped {
            AppPM(
                appLauncher = get()
            )
        }
    }
}