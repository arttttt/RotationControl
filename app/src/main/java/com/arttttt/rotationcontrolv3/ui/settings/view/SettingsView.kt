package com.arttttt.rotationcontrolv3.ui.settings.view

import com.arkivanov.mvikotlin.core.view.MviView
import com.arttttt.rotationcontrolv3.domain.entity.Settings
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem

interface SettingsView : MviView<SettingsView.Model, SettingsView.UiEvent> {

    data class Model(
        val items: List<ListItem>
    )

    sealed class UiEvent {

        data class SettingsChanged(
            val settings: Settings,
            val isChecked: Boolean,
        ) : UiEvent()
    }
}