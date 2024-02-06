package com.arttttt.rotationcontrolv3.ui.settings.transformer

import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.entity.Setting
import com.arttttt.rotationcontrolv3.domain.stores.settings.SettingsStore
import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingAdapterItem
import com.arttttt.rotationcontrolv3.ui.settings.view.SettingsView
import com.arttttt.rotationcontrolv3.utils.mvi.Transformer
import com.arttttt.rotationcontrolv3.utils.resources.ResourcesProvider
import com.arttttt.utils.unsafeCastTo
import javax.inject.Inject

class SettingsTransformer @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
) : Transformer<SettingsStore.State, SettingsView.Model> {

    override fun invoke(state: SettingsStore.State): SettingsView.Model {
        return SettingsView.Model(
            items = state.settings.map { appSettings ->
                when (appSettings) {
                    is Setting.StartOnBoot -> SettingAdapterItem.BooleanSetting(
                        type = appSettings::class.unsafeCastTo(),
                        title = resourcesProvider.getString(R.string.start_on_boot),
                        value = appSettings.value,
                    )
                    is Setting.ForcedMode -> SettingAdapterItem.BooleanSetting(
                        type = appSettings::class.unsafeCastTo(),
                        title = resourcesProvider.getString(R.string.forced_mode),
                        value = appSettings.value,
                    )
                }
            }
        )
    }
}