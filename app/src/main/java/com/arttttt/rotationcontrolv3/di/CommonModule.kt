package com.arttttt.rotationcontrolv3.di

import android.app.NotificationManager
import android.content.Context
import android.view.WindowManager
import com.arttttt.rotationcontrolv3.utils.AccelerometerObserver
import com.arttttt.rotationcontrolv3.device.services.rotation.RotationService
import com.arttttt.rotationcontrolv3.device.services.rotation.helper.IRotationServiceHelper
import com.arttttt.rotationcontrolv3.presentation.delegate.applauncher.AppLauncher
import com.arttttt.rotationcontrolv3.presentation.delegate.applauncher.IAppLauncher
import com.arttttt.rotationcontrolv3.utils.*
import com.arttttt.rotationcontrolv3.utils.delegates.errordispatcher.ErrorDispatcher
import com.arttttt.rotationcontrolv3.utils.delegates.errordispatcher.IErrorDispatcher
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.base.IPermissionResultHelper
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.base.PermissionResultHelper
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.drawoverlays.CanDrawOverlayChecker
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.drawoverlays.CanDrawOverlayRequester
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.drawoverlays.ICanDrawOverlayChecker
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.drawoverlays.ICanDrawOverlayRequester
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings.CanWriteSettingsChecker
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings.CanWriteSettingsRequester
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings.ICanWriteSettingsChecker
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings.ICanWriteSettingsRequester
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.IPreferencesDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.PreferencesDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.resources.IResourcesDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.resources.ResourcesDelegate
import com.arttttt.rotationcontrolv3.utils.extensions.koilin.unsafeCastTo
import com.arttttt.rotationcontrolv3.utils.navigation.FlowRouter
import com.arttttt.rotationcontrolv3.utils.rxjava.ISchedulersProvider
import com.arttttt.rotationcontrolv3.utils.rxjava.SchedulersProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
val commonModule = module {
    single<IPreferencesDelegate> {
        PreferencesDelegate(
            prefs = get<Context>().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        )
    }

    single<IRotationServiceHelper> { RotationService.ServiceHelper }

    val appCicerone = Cicerone.create()
    single(named(APP_ROUTER)) { appCicerone.router }
    single(named(APP_HOLDER)) { appCicerone.navigatorHolder }

    val flowCicerone = Cicerone.create(FlowRouter(appCicerone.router))
    single(named(FLOW_ROUTER)) { flowCicerone.router }
    single(named(FLOW_HOLDER)) { flowCicerone.navigatorHolder }

    single<IAppLauncher> { AppLauncher() }

    factory<IErrorDispatcher> { ErrorDispatcher() }
    factory<ISchedulersProvider> { SchedulersProvider() }

    single<IResourcesDelegate> {
        ResourcesDelegate(
            resources = get<Context>().resources
        )
    }

    single<ICanWriteSettingsChecker> {
        CanWriteSettingsChecker(
            context = get()
        )
    }

    single<ICanWriteSettingsRequester> {
        CanWriteSettingsRequester(
            context = get(),
            helper = get(),
            canWriteSettingsChecker = get()
        )
    }

    factory { CompositeDisposable() }

    single<ICanDrawOverlayChecker> {
        CanDrawOverlayChecker(
            context = get()
        )
    }

    single<ICanDrawOverlayRequester> {
        CanDrawOverlayRequester(
            context = get(),
            canWriteSettingsChecker = get(),
            helper = get()
        )
    }

    single<NotificationManager> {
        get<Context>().getSystemService(Context.NOTIFICATION_SERVICE).unsafeCastTo()
    }

    single<WindowManager> {
        get<Context>().getSystemService(Context.WINDOW_SERVICE).unsafeCastTo()
    }

    single<IViewProvider> {
        ViewProvider(
            context = get()
        )
    }

    single {
        AccelerometerObserver(
            contentResolver = get<Context>().contentResolver
        )
    }

    single<IPermissionResultHelper> { PermissionResultHelper() } bind Consumer::class
}