package com.arttttt.rotationcontrolv3.ui.settings.adapter.delegates

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.arttttt.adapterdelegates.dsl.adapterDelegate
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.entity.settings.SettingKey
import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingAdapterItem
import timber.log.Timber

fun BooleanSettingAdapterDelegate(
    onCheckedChanged: (SettingKey<Boolean>, Boolean) -> Unit,
) = adapterDelegate<SettingAdapterItem<Boolean>>(
    layout = R.layout.item_settings,
    layoutInflater = ::inflateSettingItem,
) {

    val switcher = findViewById<CompoundButton>(R.id.switcher)

    switcher.setOnCheckedChangeListener { _, isChecked ->
        onCheckedChanged.invoke(item.key, isChecked)
    }

    bind {
        switcher.text = item.title
        switcher.isChecked = item.value
    }
}

/**
 * Inflates the setting item, falling back to a framework [android.widget.Switch] layout when the
 * Material switch fails to inflate. On some devices the AppCompat switch thumb drawable
 * (abc_switch_thumb_material) cannot be resolved, which otherwise crashes the whole screen.
 */
private fun inflateSettingItem(parent: ViewGroup, layoutRes: Int): View {
    val inflater = LayoutInflater.from(parent.context)

    return try {
        inflater.inflate(layoutRes, parent, false)
    } catch (e: Exception) {
        Timber.e(e, "Failed to inflate setting item, falling back to a framework switch")

        inflater.inflate(R.layout.item_settings_fallback, parent, false)
    }
}
