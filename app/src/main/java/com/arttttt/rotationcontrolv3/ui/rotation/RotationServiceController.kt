package com.arttttt.rotationcontrolv3.ui.rotation

import android.app.Notification
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.rotationcontrolv3.domain.entity.exceptions.NoPermissionsException
import com.arttttt.rotationcontrolv3.domain.entity.rotation.OrientationMode
import com.arttttt.rotationcontrolv3.domain.stores.rotation.RotationStore
import com.arttttt.rotationcontrolv3.ui.rotation.model.NotificationButton
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceView
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge

class RotationServiceController(
    private val rotationStore: RotationStore,
) {

    sealed interface Command {

        data object ConfigurationChanged : Command
    }

    interface PlatformCallback {

        fun onNotificationUpdated(notification: Notification)
        fun stopService()
    }

    private val _commands: MutableSharedFlow<Command> = MutableSharedFlow(
        extraBufferCapacity = 1,
    )

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
                    RotationStore.Intent.SetGlobalOrientationMode(
                        orientationMode = OrientationMode.of(event)
                    )
                }
                .bindTo(rotationStore)

            view
                .events
                .filterIsInstance<RotationServiceView.UiEvent.StopServiceClicked>()
                .bindTo {
                    platformCallback?.stopService()
                }

            merge(
                rotationStore
                    .states
                    .map { state -> state.toViewState() }
                    .distinctUntilChanged(),
                _commands
                    .filterIsInstance<Command.ConfigurationChanged>()
                    .map { rotationStore.state.toViewState() },
                view
                    .events
                    .filterIsInstance<RotationServiceView.UiEvent.NotificationDeleted>()
                    .map { rotationStore.state.toViewState() },
            )
                .bindTo { viewState ->
                    platformCallback?.onNotificationUpdated(
                        view.createNotification(viewState)
                    )
                }
        }
    }

    fun handleCommand(command: Command) {
        _commands.tryEmit(command)
    }

    private fun RotationStore.State.toViewState(): RotationServiceView.State {
        return when {
            error is NoPermissionsException -> RotationServiceView.State.PermissionsError
            error != null -> RotationServiceView.State.StartupError
            globalOrientationMode != null -> RotationServiceView.State.Active(
                selectedButton = globalOrientationMode.toNotificationButton()
            )
            else -> RotationServiceView.State.Starting
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

    private fun OrientationMode.toNotificationButton(): NotificationButton {
        return when (this) {
            is OrientationMode.Auto -> NotificationButton.Auto
            is OrientationMode.Portrait -> NotificationButton.Portrait
            is OrientationMode.PortraitReverse -> NotificationButton.PortraitReverse
            is OrientationMode.Landscape -> NotificationButton.Landscape
            is OrientationMode.LandscapeReverse -> NotificationButton.LandscapeReverse
        }
    }
}
