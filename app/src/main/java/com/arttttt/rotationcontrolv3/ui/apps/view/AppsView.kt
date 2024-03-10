package com.arttttt.rotationcontrolv3.ui.apps.view

import com.arkivanov.mvikotlin.core.view.MviView
import com.arttttt.rotationcontrolv3.domain.entity.apps.AppOrientation
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem

interface AppsView : MviView<AppsView.Model, AppsView.UiEvent> {

    data class Model(
        val items: List<ListItem>
    )

    sealed class UiEvent {

        data object EnableAccessibilityServiceClicked : UiEvent()
        data class AppClicked(
            val pkg: String
        ) : UiEvent()

        data class AppOrientationSelected(
            val pkg: String,
            val appOrientation: AppOrientation,
        ) : UiEvent()
    }

    sealed class Command {

        data class ShowOrientationDialog(
            val pkg: String
        ) : Command()
    }

    fun handleCommand(command: Command)
}