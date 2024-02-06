package com.arttttt.rotationcontrolv3.ui.rotation.view

import android.app.Notification
import android.content.Intent
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.arttttt.rotationcontrolv3.ui.rotation.model.NotificationButton
import kotlinx.coroutines.flow.Flow

interface RotationServiceView {

    sealed class State {

        data class Active(
            val selectedButton: NotificationButton
        ) : State()

        data object Error : State()
    }

    sealed class UiEvent {

        sealed class ButtonEvent : UiEvent() {
            data object AutoClicked : ButtonEvent()
            data object PortraitClicked : ButtonEvent()
            data object PortraitReverseClicked : ButtonEvent()
            data object LandscapeClicked : ButtonEvent()
            data object LandscapeReverseClicked : ButtonEvent()
        }

        data class NotificationUpdated(
            val notification: Notification,
        ) : UiEvent()
    }

    val events: Flow<UiEvent>

    fun render(state: State)

    fun handleClick(intent: Intent)
}