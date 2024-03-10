package com.arttttt.rotationcontrolv3.ui.apps.adapter.delegates

import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.apps.adapter.models.AppAdapterItem
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.dsl.adapterDelegate

fun AppAdapterDelegate(
    onClick: (String) -> Unit,
) = adapterDelegate<AppAdapterItem>(R.layout.item_app) {

    val iconImageView = findViewById<ImageView>(R.id.iconImageView)
    val titleTextView = findViewById<TextView>(R.id.titleTextView)
    val packageTextView = findViewById<TextView>(R.id.packageTextView)
    val orientationTextView = findViewById<TextView>(R.id.orientationTextView)

    itemView.setOnClickListener {
        onClick.invoke(item.appPackage)
    }

    bind {
        titleTextView.text = item.title
        packageTextView.text = item.appPackage
        orientationTextView.text = item.orientation

        iconImageView.isVisible = item.icon != null
        item.icon?.let(iconImageView::setImageDrawable)
    }
}