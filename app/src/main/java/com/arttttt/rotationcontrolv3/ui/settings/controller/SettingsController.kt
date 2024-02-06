package com.arttttt.rotationcontrolv3.ui.settings.controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arkivanov.mvikotlin.extensions.coroutines.states
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.domain.stores.settings.SettingsStore
import com.arttttt.rotationcontrolv3.ui.settings.transformer.SettingsTransformer
import com.arttttt.rotationcontrolv3.ui.settings.view.SettingsView
import com.arttttt.rotationcontrolv3.utils.mvi.Controller
import com.arttttt.utils.unsafeCastTo
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@PerScreen
class SettingsController @Inject constructor(
    private val transformer: SettingsTransformer,
    private val settingsStore: SettingsStore,
) : Controller<SettingsView> {

    override fun onViewCreated(view: SettingsView, lifecycle: Lifecycle) {
        bind(
            lifecycle = lifecycle,
            mode = BinderLifecycleMode.CREATE_DESTROY,
        ) {
            settingsStore
                .states
                .map(transformer)
                .bindTo(view)

            view
                .events
                .filterIsInstance<SettingsView.UiEvent.SettingsChanged<*>>()
                .map { event ->
                    SettingsStore.Intent.UpdateSettingValue(
                        value = event.value,
                        clazz = event.type.unsafeCastTo(),
                    )
                }
                .bindTo(settingsStore)
        }
    }
}