package com.arttttt.rotationcontrolv3.ui.apps.adapter

import com.arttttt.rotationcontrolv3.ui.apps.adapter.models.AppAdapterItem
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.EqualsDiffCallback
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.ListItem

class AppsDiffCallback : EqualsDiffCallback() {

    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return when {
            oldItem is AppAdapterItem && newItem is AppAdapterItem -> oldItem.appPackage == newItem.appPackage
            else -> super.areItemsTheSame(oldItem, newItem)
        }
    }
}