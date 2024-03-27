package com.arttttt.rotationcontrolv3.ui.main2.controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.main2.model.MenuItem
import com.arttttt.rotationcontrolv3.ui.main2.view.MainView
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService
import com.arttttt.rotationcontrolv3.utils.mvi.Controller
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class MainController @Inject constructor() : Controller<MainView> {

    private val uiState = MutableStateFlow(
        value = MainView.Model(
            isFabVisible = false,
            fabIconRes = 0,
            menuItems = setOf(
                MenuItem.Settings,
                MenuItem.Apps,
                MenuItem.About,
            ),
        )
    )

    override fun onViewCreated(
        view: MainView,
        lifecycle: Lifecycle,
    ) {
        bind(
            mainContext = Dispatchers.Main.immediate,
            lifecycle = lifecycle,
            mode = BinderLifecycleMode.CREATE_DESTROY
        ) {
            uiState.bindTo(view)

            RotationService
                .status
                .map { status -> status.toFabIconRes() }
                .bindTo { fabIconRes ->
                    uiState.update { state ->
                        state.copy(
                            fabIconRes = fabIconRes,
                        )
                    }
                }

            view
                .events
                .filterIsInstance<MainView.UiEvent.BottomNavigationClicked>()
                .bindTo { event ->
                    uiState.update { state ->
                        state.copy(
                            isFabVisible = event.isFabVisible
                        )
                    }

                    view.handleCommand(
                        command = MainView.Command.SetMenuItem(
                            item = MenuItem.of(event.id)
                        )
                    )
                }
        }
    }

    private fun RotationService.Status.toFabIconRes(): Int {
        return when (this) {
            RotationService.Status.HALTED -> R.drawable.ic_start
            RotationService.Status.RUNNING -> R.drawable.ic_stop
        }
    }

    private val MainView.UiEvent.BottomNavigationClicked.isFabVisible: Boolean
        get() {
            return id == MenuItem.Settings.id
        }

    private fun MenuItem.Companion.of(id: Int): MenuItem {
        return when (id) {
            MenuItem.Settings.id -> MenuItem.Settings
            MenuItem.Apps.id -> MenuItem.Apps
            MenuItem.About.id -> MenuItem.About
            else -> throw IllegalArgumentException("unknown menu item id: $id")
        }
    }
}