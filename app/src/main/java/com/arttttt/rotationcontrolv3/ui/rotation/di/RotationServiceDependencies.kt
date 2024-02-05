package com.arttttt.rotationcontrolv3.ui.rotation.di

import android.content.Context
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository

interface RotationServiceDependencies {

    val context: Context
    val permissionsRepository: PermissionsRepository
    val settingsRepository: SettingsRepository
}