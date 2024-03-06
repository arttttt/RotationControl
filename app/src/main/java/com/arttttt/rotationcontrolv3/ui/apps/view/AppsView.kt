package com.arttttt.rotationcontrolv3.ui.apps.view

import com.arkivanov.mvikotlin.core.view.MviView
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem

interface AppsView : MviView<AppsView.Model, AppsView.UiEvent> {

    data class Model(
        val items: List<ListItem>
    )

    sealed class UiEvent
}