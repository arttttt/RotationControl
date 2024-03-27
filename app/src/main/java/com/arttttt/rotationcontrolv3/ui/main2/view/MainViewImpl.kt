package com.arttttt.rotationcontrolv3.ui.main2.view

import android.view.View
import com.arkivanov.mvikotlin.core.utils.diff
import com.arkivanov.mvikotlin.core.view.BaseMviView
import com.arkivanov.mvikotlin.core.view.ViewRenderer
import com.arttttt.rotationcontrolv3.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainViewImpl(
    view: View,
) : BaseMviView<MainView.Model, MainView.UiEvent>(), MainView {

    private val fab: FloatingActionButton = view.findViewById(R.id.fab)

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
    }
}