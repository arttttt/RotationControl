package com.arttttt.rotationcontrolv3.presentation.feature

import com.arttttt.rotationcontrolv3.presentation.feature.mainmenu.presenter.MainContract
import com.arttttt.rotationcontrolv3.presentation.feature.mainmenu.presenter.MainPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val hostModule = module {
    scope(named<HostActivity>()) {
        scoped<MainContract.Presenter> {
            MainPresenter(
                get(),
                get()
            )
        }
    }
}