package com.arttttt.rotationcontrolv3.di

import com.arttttt.rotationcontrolv3.model.permissions.AppPermissions
import com.arttttt.rotationcontrolv3.model.permissions.base.PermissionsChecker
import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences
import com.arttttt.rotationcontrolv3.model.services.RotationService
import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.presenter.about.AboutContract
import com.arttttt.rotationcontrolv3.presenter.about.AboutPresenter
import com.arttttt.rotationcontrolv3.presenter.main.MainContract
import com.arttttt.rotationcontrolv3.presenter.main.MainPresenter
import com.arttttt.rotationcontrolv3.presenter.settings.*
import org.koin.dsl.module.module

object AppModule {
    val module = module {
        single { AppPermissions()}
        single { AppPreferences(get())}
        single { get<AppPermissions>() as PermissionsChecker }
        single { RotationService.Companion as ServiceHelper }

        single { MainPresenter(get(), get()) as MainContract.Presenter }
        single { SettingsPresenter(get()) as SettingsContract.Presenter }
        single { AboutPresenter() as AboutContract.Presenter }
    }
}