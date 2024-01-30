package com.arttttt.rotationcontrolv3.ui.rotation.view

import android.app.Notification
import android.content.Intent
import kotlinx.coroutines.flow.Flow

interface RotationServiceView {

    sealed class UiEvent {

        sealed class ButtonEvent : UiEvent() {
            data object AutoClicked : ButtonEvent()
            data object PortraitClicked : ButtonEvent()
            data object PortraitReverseClicked : ButtonEvent()
            data object LandscapeClicked : ButtonEvent()
            data object LandscapeReverseClicked : ButtonEvent()
        }
    }

    val events: Flow<UiEvent>

    fun createNotification(channelId: String): Notification

    fun handleClick(intent: Intent)
}