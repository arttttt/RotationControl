package com.arttttt.rotationcontrolv3.ui.rotation.view

import android.app.Notification
import android.content.Intent
import com.arttttt.rotationcontrolv3.ui.rotation.model.NotificationButton
import kotlinx.coroutines.flow.Flow

interface RotationServiceView {

    sealed interface State {

        data object Starting : State

        data class Active(
            val selectedButton: NotificationButton
        ) : State

        data object PermissionsError : State

        data object StartupError : State
    }

    sealed interface UiEvent {

        sealed class ButtonEvent : UiEvent {
            data object AutoClicked : ButtonEvent()
            data object PortraitClicked : ButtonEvent()
            data object PortraitReverseClicked : ButtonEvent()
            data object LandscapeClicked : ButtonEvent()
            data object LandscapeReverseClicked : ButtonEvent()
        }

        data object StopServiceClicked : UiEvent

        data object NotificationDeleted : UiEvent
    }

    val events: Flow<UiEvent>

    fun createNotification(model: State): Notification

    fun handleAction(intent: Intent)
}