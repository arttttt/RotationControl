package com.arttttt.rotationcontrolv3.presentation.feature.about.di

import com.arttttt.rotationcontrolv3.presentation.feature.about.presenter.AboutContract
import com.arttttt.rotationcontrolv3.presentation.feature.about.presenter.AboutPresenter
import com.arttttt.rotationcontrolv3.presentation.feature.about.view.AboutFragment
import org.koin.core.qualifier.named
import org.koin.dsl.module

val aboutModule = module {
    scope(named<AboutFragment>()) {
        scoped<AboutContract.Presenter> { AboutPresenter() }
    }
}