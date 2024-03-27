package com.arttttt.rotationcontrolv3.ui.main2.view

import com.arkivanov.mvikotlin.core.view.MviView
import com.arttttt.rotationcontrolv3.ui.main2.model.MenuItem

interface MainView : MviView<MainView.Model, MainView.UiEvent> {

    data class Model(
        val isFabVisible: Boolean,
        val fabIconRes: Int,
        val menuItems: Set<MenuItem>,
    )

    sealed class UiEvent {

        data class BottomNavigationClicked(val id: Int) : UiEvent()
    }

    sealed class Command {

        data class SetMenuItem(val item: MenuItem) : Command()
    }

    fun handleCommand(command: Command)
}