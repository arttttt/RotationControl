package com.arttttt.rotationcontrolv3.presentation.feature.mainflow.di

import com.arttttt.rotationcontrolv3.presentation.delegate.screenswitcher.IScreenSwitcherDelegate
import com.arttttt.rotationcontrolv3.presentation.delegate.screenswitcher.IScreenSwitcherFragmentManagerHolder
import com.arttttt.rotationcontrolv3.presentation.delegate.screenswitcher.ScreenSwitcherDelegate
import com.arttttt.rotationcontrolv3.presentation.feature.mainflow.pm.MainFlowPM
import com.arttttt.rotationcontrolv3.presentation.feature.mainflow.view.MainFlowFragment
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mainFlowModule = module {
    scope(named<MainFlowFragment>()) {
        scoped<IScreenSwitcherDelegate> { (containerId: Int) ->
            ScreenSwitcherDelegate(
                containerId = containerId
            )
        }

        scoped<IScreenSwitcherFragmentManagerHolder> {
            ScreenSwitcherDelegate.Companion
        }

        scoped { (containerId: Int) ->
            MainFlowPM(
                rotationServiceHelper = get(),
                canWriteSettingsChecker = get(),
                canWriteSettingsRequester = get(),
                preferencesDelegate = get(),
                canDrawOverlayChecker = get(),
                canDrawOverlayRequester = get(),
                screenSwitcher = get { parametersOf(containerId) }
            )
        }
    }
}