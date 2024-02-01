package com.arttttt.rotationcontrolv3.ui.settings.view

import com.arkivanov.mvikotlin.core.view.MviView
import com.arttttt.rotationcontrolv3.domain.entity.Setting
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem
import kotlin.reflect.KClass

interface SettingsView : MviView<SettingsView.Model, SettingsView.UiEvent> {

    data class Model(
        val items: List<ListItem>
    )

    sealed class UiEvent {

        data class SettingsChanged<T>(
            val type: KClass<out Setting<T>>,
            val value: T,
        ) : UiEvent()
    }
}