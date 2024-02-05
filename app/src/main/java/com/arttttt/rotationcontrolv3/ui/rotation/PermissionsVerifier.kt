package com.arttttt.rotationcontrolv3.ui.rotation

import com.arttttt.permissions.utils.extensions.toBoolean
import com.arttttt.rotationcontrolv3.data.model.DrawOverlayPermission
import com.arttttt.rotationcontrolv3.data.model.NotificationsPermission
import com.arttttt.rotationcontrolv3.data.model.WriteSettingsPermission
import com.arttttt.rotationcontrolv3.domain.entity.Setting
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository

class PermissionsVerifier(
    private val permissionsRepository: PermissionsRepository,
    private val settingsRepository: SettingsRepository
) {

    suspend fun areAllPermissionsGranted(): Boolean {
        val canShowNotifications = permissionsRepository.checkPermission(NotificationsPermission).toBoolean()
        val canWriteSystemSettings = permissionsRepository.checkPermission(WriteSettingsPermission).toBoolean()
        val canDrawOverlay = if (settingsRepository.getSetting(Setting.ForcedMode::class).value) {
            permissionsRepository.checkPermission(DrawOverlayPermission).toBoolean()
        } else {
            true
        }

        return canShowNotifications && canWriteSystemSettings && canDrawOverlay
    }
}