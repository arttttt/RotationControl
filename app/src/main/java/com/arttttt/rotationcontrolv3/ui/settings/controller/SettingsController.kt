package com.arttttt.rotationcontrolv3.ui.settings.controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.domain.entity.AppSettings
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingsAdapterItem
import com.arttttt.rotationcontrolv3.ui.settings.view.SettingsView
import com.arttttt.rotationcontrolv3.utils.mvi.Controller
import kotlinx.coroutines.flow.filterIsInstance
import javax.inject.Inject

@PerScreen
class SettingsController @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : Controller<SettingsView> {

    override fun onViewCreated(view: SettingsView, lifecycle: Lifecycle) {
        bind(
            lifecycle = lifecycle,
            mode = BinderLifecycleMode.CREATE_DESTROY,
        ) {
            view.render(
                SettingsView.Model(
                    items = settingsRepository
                        .getSettings()
                        .map { settings ->
                            when (settings) {
                                is AppSettings.StartOnBoot -> {
                                SettingsAdapterItem(
                                    title = "start on boot",
                                    isChecked = settings.value,
                                )
                                }
                            }
                        }
                )
            )

            view
                .events
                .filterIsInstance<SettingsView.UiEvent.SettingsChanged>()
                .bindTo { event ->
                    settingsRepository.saveSettings(
                        AppSettings.StartOnBoot(
                            value = event.isChecked,
                        )
                    )
                }
        }
    }
}