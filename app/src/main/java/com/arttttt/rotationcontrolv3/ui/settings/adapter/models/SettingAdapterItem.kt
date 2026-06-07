package com.arttttt.rotationcontrolv3.ui.settings.adapter.models

import com.arttttt.adapterdelegates.ListItem
import com.arttttt.rotationcontrolv3.domain.entity.settings.SettingKey

sealed class SettingAdapterItem<T> : ListItem {

    abstract val title: String
    abstract val key: SettingKey<T>
    abstract val value: T

    data class BooleanSetting(
        override val title: String,
        override val key: SettingKey<Boolean>,
        override val value: Boolean,
    ) : SettingAdapterItem<Boolean>()
}
