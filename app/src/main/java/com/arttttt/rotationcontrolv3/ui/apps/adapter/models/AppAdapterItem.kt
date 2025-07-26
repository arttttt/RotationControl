package com.arttttt.rotationcontrolv3.ui.apps.adapter.models

import android.graphics.drawable.Drawable
import com.arttttt.adapterdelegates.ListItem

data class AppAdapterItem(
    val title: String,
    val appPackage: String,
    val orientation: String,
    val icon: Drawable?,
) : ListItem