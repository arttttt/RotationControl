package com.arttttt.rotationcontrolv3.domain.repository

import com.arttttt.rotationcontrolv3.domain.entity.AppSettings

interface SettingsRepository {

    suspend fun saveSettings(settings: AppSettings)
    suspend fun getSettings(): List<AppSettings>
}