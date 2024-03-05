package com.arttttt.rotationcontrolv3.ui.apps.adapter.delegates

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.apps.adapter.models.AppAdapterItem
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.dsl.adapterDelegate

fun AppAdapterDelegate() = adapterDelegate<AppAdapterItem>(R.layout.item_app) {
    val titleTextView = findViewById<TextView>(R.id.titleTextView)
    val packageTextView = findViewById<TextView>(R.id.packageTextView)
    val iconImageView = findViewById<ImageView>(R.id.iconImageView)

    bind {
        titleTextView.text = item.title
        packageTextView.text = item.appPackage

        iconImageView.isVisible = item.icon != null
        item.icon?.let(iconImageView::setImageDrawable)
    }
}