package com.arttttt.rotationcontrolv3.ui.apps.controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import com.arttttt.rotationcontrolv3.ui.apps.transformer.AppsTransformer
import com.arttttt.rotationcontrolv3.ui.apps.view.AppsView
import com.arttttt.rotationcontrolv3.utils.mvi.Controller
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppsController @Inject constructor(
    private val appsStore: AppsStore,
    private val transformer: AppsTransformer,
) : Controller<AppsView> {

    override fun onViewCreated(
        view: AppsView,
        lifecycle: Lifecycle,
    ) {
        bind(
            lifecycle = lifecycle,
            mode = BinderLifecycleMode.CREATE_DESTROY,
        ) {
            appsStore
                .states
                .map(transformer::invoke)
                .flowOn(Dispatchers.IO)
                .bindTo(view)
        }
    }
}