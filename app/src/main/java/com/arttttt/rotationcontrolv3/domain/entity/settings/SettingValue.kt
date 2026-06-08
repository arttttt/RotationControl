package com.arttttt.rotationcontrolv3.domain.entity.settings

/**
 * A [SettingKey] paired with its current [value]. Used to carry a snapshot of a setting through the
 * store and UI.
 */
data class SettingValue<T>(
    val key: SettingKey<T>,
    val value: T,
)
