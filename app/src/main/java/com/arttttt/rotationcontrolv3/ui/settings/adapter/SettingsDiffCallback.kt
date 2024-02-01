package com.arttttt.rotationcontrolv3.ui.settings.adapter

import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingAdapterItem
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.EqualsDiffCallback
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem

class SettingsDiffCallback : EqualsDiffCallback() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return when {
            oldItem is SettingAdapterItem<*> && newItem is SettingAdapterItem<*> -> oldItem.type == newItem.type
            else -> super.areItemsTheSame(oldItem, newItem)
        }
    }
}