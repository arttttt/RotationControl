package com.arttttt.rotationcontrolv3.ui.settings.adapter.delegates

import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.entity.settings.Setting
import com.arttttt.rotationcontrolv3.ui.settings.adapter.models.SettingAdapterItem
import com.arttttt.rotationcontrolv3.utils.adapterdelegates.dsl.adapterDelegate
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlin.reflect.KClass

fun BooleanSettingAdapterDelegate(
    onCheckedChanged: (KClass<out Setting<Boolean>>, Boolean) -> Unit,
) = adapterDelegate<SettingAdapterItem<Boolean>>(R.layout.item_settings) {

    val switcher = findViewById<SwitchMaterial>(R.id.switcher)

    switcher.setOnCheckedChangeListener { _, isChecked ->
        onCheckedChanged.invoke(item.type, isChecked)
    }

    bind {
        switcher.text = item.title
        switcher.isChecked = item.value
    }
}