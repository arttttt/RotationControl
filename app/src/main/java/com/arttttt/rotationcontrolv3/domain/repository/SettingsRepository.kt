package com.arttttt.rotationcontrolv3.domain.repository

import com.arttttt.rotationcontrolv3.domain.entity.Setting
import kotlin.reflect.KClass

interface SettingsRepository {

    suspend fun <T> getSetting(clazz: KClass<out Setting<T>>): Setting<T>
    suspend fun <T> saveSetting(clazz: KClass<out Setting<T>>, value: T)
    suspend fun getAllSettings(): List<Setting<*>>
}