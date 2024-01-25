package com.arttttt.rotationcontrolv3.ui.settings.transformer

import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.entity.AppSettings
import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingsAdapterItem
import com.arttttt.rotationcontrolv3.ui.settings.view.SettingsView
import com.arttttt.rotationcontrolv3.utils.mvi.Transformer
import com.arttttt.rotationcontrolv3.utils.resources.ResourcesProvider
import javax.inject.Inject

class SettingsTransformer @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
) : Transformer<List<AppSettings>, SettingsView.Model> {

    override fun invoke(settings: List<AppSettings>): SettingsView.Model {
        return SettingsView.Model(
            items = settings.map { appSettings ->
                when (appSettings) {
                    is AppSettings.StartOnBoot -> SettingsAdapterItem(
                        type = appSettings::class,
                        title = resourcesProvider.getString(R.string.start_on_boot),
                        isChecked = appSettings.value,
                    )
                }
            }
        )
    }
}