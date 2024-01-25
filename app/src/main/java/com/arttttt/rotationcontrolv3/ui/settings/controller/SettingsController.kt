package com.arttttt.rotationcontrolv3.ui.settings.controller

import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.mvikotlin.core.binder.BinderLifecycleMode
import com.arkivanov.mvikotlin.extensions.coroutines.bind
import com.arkivanov.mvikotlin.extensions.coroutines.events
import com.arttttt.rotationcontrolv3.di.scopes.PerScreen
import com.arttttt.rotationcontrolv3.domain.entity.AppSettings
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.ui.settings.transformer.SettingsTransformer
import com.arttttt.rotationcontrolv3.ui.settings.view.SettingsView
import com.arttttt.rotationcontrolv3.utils.mvi.Controller
import kotlinx.coroutines.flow.filterIsInstance
import javax.inject.Inject

@PerScreen
class SettingsController @Inject constructor(
    private val transformer: SettingsTransformer,
    private val settingsRepository: SettingsRepository,
) : Controller<SettingsView> {

    private var settings = settingsRepository.getSettings()

    override fun onViewCreated(view: SettingsView, lifecycle: Lifecycle) {
        bind(
            lifecycle = lifecycle,
            mode = BinderLifecycleMode.CREATE_DESTROY,
        ) {
            view.render()

            view
                .events
                .filterIsInstance<SettingsView.UiEvent.SettingsChanged>()
                .bindTo { event ->
                    val value = settings.find { it::class == event.type }

                    if (value is AppSettings.StartOnBoot) {
                        settingsRepository.saveSettings(
                            value.copy(
                                value = event.isChecked,
                            )
                        )

                        settings = settingsRepository.getSettings()

                        view.render()
                    }
                }
        }
    }

    private fun SettingsView.render() {
        render(
            transformer.invoke(settings)
        )
    }
}