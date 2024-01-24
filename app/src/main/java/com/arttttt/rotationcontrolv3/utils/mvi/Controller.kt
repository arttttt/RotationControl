package com.arttttt.rotationcontrolv3.utils.mvi

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.view.MviView

interface Controller<V : MviView<*, *>> {

    fun onViewCreated(
        view: V,
        lifecycle: Lifecycle,
    )
}