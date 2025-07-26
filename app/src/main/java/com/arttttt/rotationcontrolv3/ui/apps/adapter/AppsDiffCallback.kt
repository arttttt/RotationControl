package com.arttttt.rotationcontrolv3.ui.apps.adapter

import com.arttttt.adapterdelegates.EqualsDiffCallback
import com.arttttt.adapterdelegates.ListItem
import com.arttttt.rotationcontrolv3.ui.apps.adapter.models.AppAdapterItem

class AppsDiffCallback : EqualsDiffCallback() {

    override fun areItemsTheSame(
        oldItem: ListItem,
        newItem: ListItem,
    ): Boolean {
        return when {
            oldItem is AppAdapterItem && newItem is AppAdapterItem -> oldItem.appPackage == newItem.appPackage
            else -> super.areItemsTheSame(oldItem, newItem)
        }
    }
}