package com.arttttt.rotationcontrolv3.ui.settings.adapter.delegates

import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.entity.AppSettings
import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingsAdapterItem
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.dsl.adapterDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.reflect.KClass

fun SettingsAdapterDelegate(
    onCheckedChanged: (KClass<out AppSettings>, Boolean) -> Unit,
) = adapterDelegate<SettingsAdapterItem>(R.layout.item_settings) {

    val switcher = findViewById<SwitchMaterial>(R.id.switcher)

    switcher.setOnCheckedChangeListener { _, isChecked ->
        onCheckedChanged.invoke(item.type, isChecked)
    }

    bind {
        switcher.text = item.title
        switcher.isChecked = item.isChecked
    }
}