package com.arttttt.rotationcontrolv3.di

import com.arttttt.rotationcontrolv3.model.permissions.AppPermissions
import com.arttttt.rotationcontrolv3.model.permissions.base.PermissionsChecker
import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences
import com.arttttt.rotationcontrolv3.model.services.RotationService
import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.presenter.SettingsContract
import com.arttttt.rotationcontrolv3.presenter.SettingsPresenter
import org.koin.dsl.module.module

object AppModule {
    val module = module {
        single { AppPermissions()}
        single { AppPreferences(get())}
        single { get<AppPermissions>() as PermissionsChecker }
        single { RotationService.Companion as ServiceHelper }
        single { SettingsPresenter(get(), get()) as SettingsContract.Presenter }
    }
}