package com.arttttt.rotationcontrolv3.ui.rotation.view

import android.app.Notification
import android.content.Intent
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.arttttt.rotationcontrolv3.ui.rotation.model.NotificationButton
import kotlinx.coroutines.flow.Flow

interface RotationServiceView {

    data class State(
        val selectedButton: NotificationButton
    )

    sealed class UiEvent {

        sealed class ButtonEvent : UiEvent() {
            data object AutoClicked : ButtonEvent()
            data object PortraitClicked : ButtonEvent()
            data object PortraitReverseClicked : ButtonEvent()
            data object LandscapeClicked : ButtonEvent()
            data object LandscapeReverseClicked : ButtonEvent()
        }
    }

    val renderer: ViewRenderer<State>

    val events: Flow<UiEvent>

    fun handleClick(intent: Intent)
}