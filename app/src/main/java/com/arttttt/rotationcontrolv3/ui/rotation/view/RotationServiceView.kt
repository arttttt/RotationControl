package com.arttttt.rotationcontrolv3.ui.rotation.view

import android.app.Notification
import android.content.Intent
import com.arttttt.rotationcontrolv3.ui.rotation.model.NotificationButton
import kotlinx.coroutines.flow.Flow

interface RotationServiceView {

    sealed interface State {

        data class Active(
            val selectedButton: NotificationButton
        ) : State

        data object Error : State
    }

    sealed interface UiEvent {

        sealed class ButtonEvent : UiEvent {
            data object AutoClicked : ButtonEvent()
            data object PortraitClicked : ButtonEvent()
            data object PortraitReverseClicked : ButtonEvent()
            data object LandscapeClicked : ButtonEvent()
            data object LandscapeReverseClicked : ButtonEvent()
        }

        data class NotificationUpdated(
            val notification: Notification,
        ) : UiEvent

        data object StopServiceClicked : UiEvent

        data object NotificationDeleted : UiEvent
    }

    sealed interface Command {

        data class UpdateNotification(
            val selectedButton: NotificationButton
        ) : Command
    }

    val events: Flow<UiEvent>

    fun render(model: State)

    fun handleAction(intent: Intent)

    fun handleCommand(command: Command)
}