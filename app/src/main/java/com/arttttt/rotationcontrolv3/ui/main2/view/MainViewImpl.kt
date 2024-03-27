package com.arttttt.rotationcontrolv3.ui.main2.view

import android.view.View
import android.view.ViewGroup
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.main2.model.MenuItem
import com.arttttt.utils.unsafeCastTo
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainViewImpl(
    view: View,
    private val setMenuItem: (MenuItem) -> Unit,
) : BaseMviView<MainView.Model, MainView.UiEvent>(), MainView {

    private val fab: FloatingActionButton = view.findViewById(R.id.fab)
    private val bottomNavigation: BottomNavigationView = view.findViewById(R.id.bottomNavigation)

    override val renderer: ViewRenderer<MainView.Model> = diff {
        diff(
            get = MainView.Model::isFabVisible,
            set = { isVisible ->
                  if (isVisible) {
                      fab.show()
                  } else {
                      fab.hide()
                  }
            },
        )

        diff(
            get = MainView.Model::fabIconRes,
            set = fab::setImageResource,
        )

        diff(
            get = MainView.Model::menuItems,
            set = { items ->
                items.forEach { item ->
                    bottomNavigation.addMenuItem(item)
                }
            }
        )
    }

    init {
        view.unsafeCastTo<ViewGroup>().bringChildToFront(fab)

        bottomNavigation.setOnItemSelectedListener { item ->
            dispatch(MainView.UiEvent.BottomNavigationClicked(item.itemId))

            true
        }
    }

    override fun handleCommand(command: MainView.Command) {
        when (command) {
            is MainView.Command.SetMenuItem -> setMenuItem.invoke(command.item)
        }
    }

    private fun BottomNavigationView.addMenuItem(item: MenuItem) {
        menu.add(0, item.id, 0, item.title).apply {
            setIcon(item.icon)
        }
    }
}