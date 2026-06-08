package com.arttttt.rotationcontrolv3.ui.settings.view

import com.arkivanov.mvikotlin.core.view.MviView
import com.arttttt.adapterdelegates.ListItem
import com.arttttt.rotationcontrolv3.domain.entity.settings.SettingKey

interface SettingsView : MviView<SettingsView.Model, SettingsView.UiEvent> {

    data class Model(
        val items: List<ListItem>
    )

    sealed class UiEvent {

        data class SettingsChanged<T>(
            val key: SettingKey<T>,
            val value: T,
        ) : UiEvent()
    }
}
