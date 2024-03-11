package com.arttttt.rotationcontrolv3.framework.services

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arttttt.rotationcontrolv3.domain.stores.apporientaton.AppOrientationStore
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppOrientationAccessibilityServiceController @Inject constructor(
    private val appsStore: AppsStore,
    private val appOrientationStore: AppOrientationStore,
) {

    fun onCreated(
        lifecycle: Lifecycle,
        events: Flow<AppOrientationAccessibilityService.AccessibilityServiceEvent>,
    ) {
        bind(
            mainContext = Dispatchers.Main.immediate,
            lifecycle = lifecycle,
            mode = BinderLifecycleMode.CREATE_DESTROY,
        ) {
            events
                .filterIsInstance<AppOrientationAccessibilityService.AccessibilityServiceEvent.WindowChanged>()
                .map { event ->
                    AppOrientationStore.Intent.ChangeLaunchedApp(
                        pkg = event.pkg
                    )
                }
                .bindTo(appOrientationStore)
        }
    }
}