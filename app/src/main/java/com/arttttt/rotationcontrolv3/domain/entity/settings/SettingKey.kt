package com.arttttt.rotationcontrolv3.domain.entity.settings

/**
 * Type-safe identifier for a single app setting. Each key fixes the value type [T] and its default,
 * so the settings repository can read/write without unchecked casts on the call sites.
 */
sealed class SettingKey<T>(
    val default: T,
) {

    data object StartOnBoot : SettingKey<Boolean>(default = false)

    data object ForcedMode : SettingKey<Boolean>(default = false)
}
