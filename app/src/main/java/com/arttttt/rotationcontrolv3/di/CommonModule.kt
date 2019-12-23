package com.arttttt.rotationcontrolv3.di

import android.app.NotificationManager
import android.content.Context
import android.view.WindowManager
import com.arttttt.rotationcontrolv3.device.services.rotation.RotationService
import com.arttttt.rotationcontrolv3.device.services.rotation.helper.IRotationServiceHelper
import com.arttttt.rotationcontrolv3.presentation.delegate.applauncher.AppLauncher
import com.arttttt.rotationcontrolv3.presentation.delegate.applauncher.IAppLauncher
import com.arttttt.rotationcontrolv3.utils.*
import com.arttttt.rotationcontrolv3.utils.delegates.errordispatcher.ErrorDispatcher
import com.arttttt.rotationcontrolv3.utils.delegates.errordispatcher.IErrorDispatcher
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.PermissionsManager
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.actions.DrawOverlayAction
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.actions.PermissionAction
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.actions.WriteSystemSettings
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.helper.ActivityHolder
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.helper.StartForResult
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.helper.PermissionHelper
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.IPreferencesDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.PreferencesDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.resources.IResourcesDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.resources.ResourcesDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.toast.IToastDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.toast.ToastDelegate
import com.arttttt.rotationcontrolv3.utils.extensions.koilin.unsafeCastTo
import com.arttttt.rotationcontrolv3.utils.navigation.FlowRouter
import com.arttttt.rotationcontrolv3.utils.rxjava.ISchedulersProvider
import com.arttttt.rotationcontrolv3.utils.rxjava.SchedulersProvider
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
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

    single<IToastDelegate> {
        ToastDelegate(
            context = get()
        )
    }

    factory { CompositeDisposable() }

    single<Consumer<Int>> { PermissionHelper() } binds arrayOf(ActivityHolder::class, StartForResult::class)

    single<PermissionAction>(named("drawOverlays")) {
        DrawOverlayAction(
            context = get()
        )
    }

    single<PermissionAction>(named("writeSystemSettings")) {
        WriteSystemSettings(
            context = get()
        )
    }

    single {
        PermissionsManager(
            startForResult = get(),
            actions = getKoin().getAll<PermissionAction>().toSet()
        )
    }
}