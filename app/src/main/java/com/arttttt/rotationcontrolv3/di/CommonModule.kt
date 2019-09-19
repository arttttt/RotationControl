package com.arttttt.rotationcontrolv3.di

import android.content.Context
import com.arttttt.rotationcontrolv3.device.services.RotationService
import com.arttttt.rotationcontrolv3.device.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.utils.PREFERENCES_NAME
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.AppPermissions
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.IPermissionsCheckerDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.IPermissionsRequesterDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.PreferencesDelegate
import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {
    single<IPermissionsCheckerDelegate> {
        AppPermissions(
            context = get()
        )
    } bind IPermissionsRequesterDelegate::class

    single {
        PreferencesDelegate(
            prefs = get<Context>().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        )
    }

    single<ServiceHelper> { RotationService.Companion }
}