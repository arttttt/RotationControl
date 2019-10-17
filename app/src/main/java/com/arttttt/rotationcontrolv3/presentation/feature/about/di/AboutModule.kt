package com.arttttt.rotationcontrolv3.presentation.feature.about.di

import com.arttttt.rotationcontrolv3.presentation.feature.about.pm.AboutPM
import com.arttttt.rotationcontrolv3.presentation.feature.about.view.AboutFragment
import org.koin.core.qualifier.named
import org.koin.dsl.module

val aboutModule = module {
    scope(named<AboutFragment>()) {
        scoped {
            AboutPM(
                resourcesDelegate = get()
            )
        }
    }
}