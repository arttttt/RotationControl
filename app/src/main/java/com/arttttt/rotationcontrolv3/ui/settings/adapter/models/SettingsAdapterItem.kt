package com.arttttt.rotationcontrolv3.ui.settings.adapter.models

import com.arttttt.rotationcontrolv3.domain.entity.Settings
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem

data class SettingsAdapterItem(
    val title: String,
    val isChecked: Boolean,
    val settings: Settings,
) : ListItem