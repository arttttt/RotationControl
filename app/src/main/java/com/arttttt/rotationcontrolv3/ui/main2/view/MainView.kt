package com.arttttt.rotationcontrolv3.ui.main2.view

import com.arkivanov.mvikotlin.core.view.MviView

interface MainView : MviView<MainView.Model, MainView.UiEvent> {

    data class Model(
        val value: Int = 0,
    )

    sealed class UiEvent
}