package com.arttttt.rotationcontrolv3.ui.rotation

import android.app.Notification
import android.content.Intent
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.rotationcontrolv3.domain.entity.NoPermissionsException
import com.arttttt.rotationcontrolv3.domain.entity.OrientationMode
import com.arttttt.rotationcontrolv3.domain.stores.rotation.RotationStore
import com.arttttt.rotationcontrolv3.ui.rotation.model.NotificationButton
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceView
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull

class RotationServiceController(
    private val rotationStore: RotationStore,
) {

    interface PlatformCallback {

        fun onNotificationUpdated(notification: Notification)
        fun stopService(payload: Intent?)
    }

    var platformCallback: PlatformCallback? = null

    fun onViewCreated(
        view: RotationServiceView,
        lifecycle: Lifecycle,
    ) {
        bind(
            lifecycle = lifecycle,
            mode = BinderLifecycleMode.CREATE_DESTROY
        ) {
            view
                .events
                .filterIsInstance<RotationServiceView.UiEvent.ButtonEvent>()
                .map { event ->
                    RotationStore.Intent.SetOrientationMode(
                        orientationMode = OrientationMode.of(event)
                    )
                }
                .bindTo(rotationStore)

            view
                .events
                .filterIsInstance<RotationServiceView.UiEvent.NotificationUpdated>()
                .bindTo { event ->
                    platformCallback?.onNotificationUpdated(event.notification)
                }

            view
                .events
                .filterIsInstance<RotationServiceView.UiEvent.StopServiceClicked>()
                .bindTo { event ->
                    platformCallback?.stopService(event.payload)
                }

            rotationStore
                .states
                .mapNotNull { state ->
                    when {
                        state.error is NoPermissionsException -> RotationServiceView.State.Error
                        state.orientationMode != null -> RotationServiceView.State.Active(
                            selectedButton = state.orientationMode.toNotificationButton()
                        )
                        else -> null
                    }
                }
                .bindTo(view::render)
        }
    }

    private fun OrientationMode.Companion.of(event: RotationServiceView.UiEvent.ButtonEvent): OrientationMode {
        return when (event) {
            RotationServiceView.UiEvent.ButtonEvent.AutoClicked -> OrientationMode.Auto
            RotationServiceView.UiEvent.ButtonEvent.PortraitClicked -> OrientationMode.Portrait
            RotationServiceView.UiEvent.ButtonEvent.PortraitReverseClicked -> OrientationMode.PortraitReverse
            RotationServiceView.UiEvent.ButtonEvent.LandscapeClicked -> OrientationMode.Landscape
            RotationServiceView.UiEvent.ButtonEvent.LandscapeReverseClicked -> OrientationMode.LandscapeReverse
        }
    }

    private fun OrientationMode.toNotificationButton():NotificationButton {
        return when (this) {
            is OrientationMode.Auto -> NotificationButton.Auto
            is OrientationMode.Portrait -> NotificationButton.Portrait
            is OrientationMode.PortraitReverse -> NotificationButton.PortraitReverse
            is OrientationMode.Landscape -> NotificationButton.Landscape
            is OrientationMode.LandscapeReverse -> NotificationButton.LandscapeReverse
        }
    }
}