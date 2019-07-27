package com.arttttt.rotationcontrolv3.di

import com.arttttt.rotationcontrolv3.model.permissions.AppPermissions
import com.arttttt.rotationcontrolv3.model.permissions.base.PermissionsChecker
import com.arttttt.rotationcontrolv3.model.permissions.base.PermissionsRequester
import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences
import com.arttttt.rotationcontrolv3.model.services.RotationService
import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.presenter.about.AboutContract
import com.arttttt.rotationcontrolv3.presenter.about.AboutPresenter
import com.arttttt.rotationcontrolv3.presenter.main.MainContract
import com.arttttt.rotationcontrolv3.presenter.main.MainPresenter
import com.arttttt.rotationcontrolv3.presenter.settings.*
import org.koin.dsl.bind
import org.koin.dsl.module

object AppModule {
    val module = module {
        single<PermissionsChecker> { AppPermissions() } bind PermissionsRequester::class
        single { AppPreferences(get())}
        single<ServiceHelper> { RotationService.Companion }

        single<MainContract.Presenter> { MainPresenter(get(), get()) }
        single<SettingsContract.Presenter> { SettingsPresenter(get()) }
        single<AboutContract.Presenter> { AboutPresenter() }
    }
}