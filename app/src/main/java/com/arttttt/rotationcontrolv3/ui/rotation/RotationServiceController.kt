package com.arttttt.rotationcontrolv3.ui.rotation

import android.app.Notification
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arttttt.rotationcontrolv3.domain.entity.OrientationMode
import com.arttttt.rotationcontrolv3.domain.entity.RotationStatus
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.ui.rotation.model.NotificationButton
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class RotationServiceController(
    private val sensorsRepository: SensorsRepository,
    private val orientationRepository: OrientationRepository,
) {

    fun interface PlatformCallback {

        fun onNotificationUpdated(notification: Notification)
    }

    data class State(
        val orientationMode: OrientationMode
    )

    var platformCallback: PlatformCallback? = null

    private val states = MutableStateFlow(
        State(
            orientationMode = OrientationMode.Auto,
        )
    )

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
                .bindTo { event ->
                    states.update {
                        it.copy(
                            orientationMode = OrientationMode.of(event)
                        )
                    }
                }

            view
                .events
                .filterIsInstance<RotationServiceView.UiEvent.NotificationUpdated>()
                .bindTo { event ->
                    platformCallback?.onNotificationUpdated(event.notification)
                }

            states
                .map { state ->
                    RotationServiceView.State.Active(
                        selectedButton = state.orientationMode.toNotificationButton()
                    )
                }
                .bindTo(view::render)

            states
                .map(State::orientationMode::get)
                .bindTo { mode ->
                    when (mode) {
                        OrientationMode.Auto -> sensorsRepository.enableRotation()
                        else -> {
                            sensorsRepository.disableRotation()
                            orientationRepository.setOrientation(mode)
                        }
                    }
                }

            sensorsRepository
                .getRotationStatuses()
                .filterIsInstance<RotationStatus.Enabled>()
                .bindTo {

                    states.update { state ->
                        state.copy(
                            orientationMode = OrientationMode.Auto
                        )
                    }
                }
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