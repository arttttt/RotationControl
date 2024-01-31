package com.arttttt.rotationcontrolv3.ui.rotation

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.entity.OrientationMode
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.ui.rotation.model.NotificationButton
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class RotationServiceController(
    private val sensorsRepository: SensorsRepository,
) {

    data class State(
        val orientationMode: OrientationMode
    )

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

            states
                .map { state ->
                    RotationServiceView.State(
                        selectedButton = state.orientationMode.toNotificationButton()
                    )
                }
                .bindTo(view.renderer)
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