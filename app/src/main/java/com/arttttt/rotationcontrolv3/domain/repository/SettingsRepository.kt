package com.arttttt.rotationcontrolv3.domain.repository

import com.arttttt.rotationcontrolv3.domain.entity.AppSettings

interface SettingsRepository {

    fun saveSettings(settings: AppSettings)
    fun getSettings(): List<AppSettings>
}