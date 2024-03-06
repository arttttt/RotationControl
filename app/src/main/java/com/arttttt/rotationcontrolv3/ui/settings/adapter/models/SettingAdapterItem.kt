package com.arttttt.rotationcontrolv3.ui.settings.adapter.models

import com.arttttt.rotationcontrolv3.domain.entity.settings.Setting
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem
import kotlin.reflect.KClass

sealed class SettingAdapterItem<T> : ListItem {

    abstract val title: String
    abstract val type: KClass<out Setting<T>>
    abstract val value: T

    data class BooleanSetting(
        override val title: String,
        override val type: KClass<out Setting<Boolean>>,
        override val value: Boolean,
    ) : SettingAdapterItem<Boolean>()
}