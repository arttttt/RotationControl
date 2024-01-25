package com.arttttt.rotationcontrolv3.ui.settings.adapter.models

import com.arttttt.rotationcontrolv3.domain.entity.AppSettings
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem
import kotlin.reflect.KClass

data class SettingsAdapterItem(
    val type: KClass<out AppSettings>,
    val title: String,
    val isChecked: Boolean,
) : ListItem