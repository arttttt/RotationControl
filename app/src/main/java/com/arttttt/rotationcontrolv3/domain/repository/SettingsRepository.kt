package com.arttttt.rotationcontrolv3.domain.repository

import com.arttttt.rotationcontrolv3.domain.entity.settings.SettingKey
import com.arttttt.rotationcontrolv3.domain.entity.settings.SettingValue

interface SettingsRepository {

    suspend fun <T> get(key: SettingKey<T>): T
    suspend fun <T> save(key: SettingKey<T>, value: T)
    suspend fun getAll(): List<SettingValue<*>>
}
