package com.arttttt.rotationcontrolv3.ui.settings.transformer

import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.entity.settings.SettingKey
import com.arttttt.rotationcontrolv3.domain.stores.settings.SettingsStore
import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingAdapterItem
import com.arttttt.rotationcontrolv3.ui.settings.view.SettingsView
import com.arttttt.rotationcontrolv3.utils.mvi.Transformer
import com.arttttt.rotationcontrolv3.utils.resources.ResourcesProvider
import javax.inject.Inject

class SettingsTransformer @Inject constructor(
    private val resourcesProvider: ResourcesProvider,
) : Transformer<SettingsStore.State, SettingsView.Model> {

    override fun invoke(state: SettingsStore.State): SettingsView.Model {
        return SettingsView.Model(
            items = state.settings.map { setting ->
                when (setting.key) {
                    SettingKey.StartOnBoot -> SettingAdapterItem.BooleanSetting(
                        key = SettingKey.StartOnBoot,
                        title = resourcesProvider.getString(R.string.start_on_boot),
                        value = setting.value as Boolean,
                    )
                    SettingKey.ForcedMode -> SettingAdapterItem.BooleanSetting(
                        key = SettingKey.ForcedMode,
                        title = resourcesProvider.getString(R.string.forced_mode),
                        value = setting.value as Boolean,
                    )
                }
            }
        )
    }
}
