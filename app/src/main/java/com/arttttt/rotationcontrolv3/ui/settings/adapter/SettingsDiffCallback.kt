package com.arttttt.rotationcontrolv3.ui.settings.adapter

import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingsAdapterItem
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.EqualsDiffCallback
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem

class SettingsDiffCallback : EqualsDiffCallback() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return when {
            oldItem is SettingsAdapterItem && newItem is SettingsAdapterItem -> oldItem.type == newItem.type
            else -> super.areItemsTheSame(oldItem, newItem)
        }
    }
}