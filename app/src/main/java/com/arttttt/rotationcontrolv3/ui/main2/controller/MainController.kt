package com.arttttt.rotationcontrolv3.ui.main2.controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arttttt.rotationcontrolv3.ui.main2.view.MainView
import com.arttttt.rotationcontrolv3.utils.mvi.Controller
import kotlinx.coroutines.Dispatchers

class MainController : Controller<MainView> {

    override fun onViewCreated(
        view: MainView,
        lifecycle: Lifecycle,
    ) {
        bind(
            mainContext = Dispatchers.Main.immediate,
            lifecycle = lifecycle,
            mode = BinderLifecycleMode.CREATE_DESTROY
        ) {}
    }
}