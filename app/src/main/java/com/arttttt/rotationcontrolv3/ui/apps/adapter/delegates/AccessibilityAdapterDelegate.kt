package com.arttttt.rotationcontrolv3.ui.apps.adapter.delegates

import android.widget.Button
import com.arttttt.adapterdelegates.dsl.adapterDelegate
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.apps.adapter.models.AccessibilityListItem

fun AccessibilityAdapterDelegate(
    onClick: () -> Unit,
) = adapterDelegate<AccessibilityListItem>(R.layout.item_accessibility) {

    val accessibilityButton = findViewById<Button>(R.id.accessibilityButton)

    accessibilityButton.setOnClickListener {
        onClick.invoke()
    }
}