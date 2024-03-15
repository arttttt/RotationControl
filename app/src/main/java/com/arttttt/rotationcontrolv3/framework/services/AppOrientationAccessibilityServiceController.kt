package com.arttttt.rotationcontrolv3.framework.services

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arttttt.rotationcontrolv3.domain.entity.apps.AppOrientation
import com.arttttt.rotationcontrolv3.domain.entity.rotation.OrientationMode
import com.arttttt.rotationcontrolv3.domain.stores.apporientaton.AppOrientationStore
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import com.arttttt.rotationcontrolv3.domain.stores.rotation.RotationStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class AppOrientationAccessibilityServiceController @Inject constructor(
    private val appOrientationStore: AppOrientationStore,
    private val rotationStore: RotationStore,
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

            appOrientationStore
                .labels
                .filterIsInstance<AppOrientationStore.Label.LaunchedAppChanged>()
                .mapNotNull { label ->
                    label
                        .appOrientation
                        .toOrientationMode()
                        ?.let(RotationStore.Intent::SetOrientationMode)
                }
                .bindTo(rotationStore)
        }
    }

    private fun AppOrientation.toOrientationMode(): OrientationMode? {
        return when (this) {
            AppOrientation.AUTO -> OrientationMode.Auto
            AppOrientation.PORTRAIT -> OrientationMode.Portrait
            AppOrientation.PORTRAIT_REVERSE -> OrientationMode.PortraitReverse
            AppOrientation.LANDSCAPE -> OrientationMode.Landscape
            AppOrientation.LANDSCAPE_REVERSE -> OrientationMode.LandscapeReverse
            AppOrientation.GLOBAL -> null
        }
    }
}