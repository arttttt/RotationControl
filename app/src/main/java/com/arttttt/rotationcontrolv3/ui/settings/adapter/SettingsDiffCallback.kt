package com.arttttt.rotationcontrolv3.ui.settings.adapter

import com.arttttt.adapterdelegates.EqualsDiffCallback
import com.arttttt.adapterdelegates.ListItem
import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingAdapterItem

class SettingsDiffCallback : EqualsDiffCallback() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return when {
            oldItem is SettingAdapterItem<*> && newItem is SettingAdapterItem<*> -> oldItem.type == newItem.type
            else -> super.areItemsTheSame(oldItem, newItem)
        }
    }
}