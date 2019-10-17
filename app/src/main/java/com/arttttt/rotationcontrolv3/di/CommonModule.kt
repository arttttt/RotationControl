package com.arttttt.rotationcontrolv3.di

import android.content.Context
import com.arttttt.rotationcontrolv3.device.services.RotationService
import com.arttttt.rotationcontrolv3.device.services.helper.IRotationServiceHelper
import com.arttttt.rotationcontrolv3.presentation.delegate.applauncher.AppLauncher
import com.arttttt.rotationcontrolv3.presentation.delegate.applauncher.IAppLauncher
import com.arttttt.rotationcontrolv3.utils.*
import com.arttttt.rotationcontrolv3.utils.delegates.errordispatcher.ErrorDispatcher
import com.arttttt.rotationcontrolv3.utils.delegates.errordispatcher.IErrorDispatcher
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.CanWriteSettingsChecker
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.CanWriteSettingsRequester
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.ICanWriteSettingsChecker
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.ICanWriteSettingsRequester
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.IPreferencesDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.PreferencesDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.resources.IResourcesDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.resources.ResourcesDelegate
import com.arttttt.rotationcontrolv3.utils.navigation.FlowRouter
import com.arttttt.rotationcontrolv3.utils.rxjava.ISchedulersProvider
import com.arttttt.rotationcontrolv3.utils.rxjava.SchedulersProvider
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.Router

val commonModule = module {
    single<IPreferencesDelegate> {
        PreferencesDelegate(
            prefs = get<Context>().getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
        )
    }

    single<IRotationServiceHelper> { RotationService.ServiceHelper }

    single<Cicerone<Router>>(named(APP_CICERONE)) { Cicerone.create() }
    single(named(APP_ROUTER)) { get<Cicerone<Router>>(named(APP_CICERONE)).router }
    single(named(APP_HOLDER)) { get<Cicerone<Router>>(named(APP_CICERONE)).navigatorHolder }

    single<Cicerone<FlowRouter>>(named(FLOW_CICERONE)) { Cicerone.create(FlowRouter(get(named(APP_ROUTER)))) }
    single(named(FLOW_ROUTER)) { get<Cicerone<FlowRouter>>(named(FLOW_CICERONE)).router }
    single(named(FLOW_HOLDER)) { get<Cicerone<FlowRouter>>(named(FLOW_CICERONE)).navigatorHolder }

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
}