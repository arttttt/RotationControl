package com.arttttt.rotationcontrolv3.ui.rotation

import com.arttttt.permissions.utils.extensions.toBoolean
import com.arttttt.rotationcontrolv3.domain.entity.settings.Setting
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.framework.model.DrawOverlayPermission
import com.arttttt.rotationcontrolv3.framework.model.NotificationsPermission
import com.arttttt.rotationcontrolv3.framework.model.WriteSettingsPermission

class PermissionsVerifier(
    private val settingsRepository: SettingsRepository,
    private val permissionsRepository: PermissionsRepository,
) {

    fun areAllPermissionsGranted(
        forced: Boolean,
    ): Boolean {
        val canShowNotifications = permissionsRepository.checkPermission(NotificationsPermission).toBoolean()
        val canWriteSystemSettings = permissionsRepository.checkPermission(WriteSettingsPermission).toBoolean()
        val canDrawOverlay = if (forced) {
            permissionsRepository.checkPermission(DrawOverlayPermission).toBoolean()
        } else {
            true
        }

        return canShowNotifications && canWriteSystemSettings && canDrawOverlay
    }

    suspend fun areAllPermissionsGranted2(): Boolean {
        val canShowNotifications = permissionsRepository.checkPermission(NotificationsPermission).toBoolean()
        val canWriteSystemSettings = permissionsRepository.checkPermission(WriteSettingsPermission).toBoolean()
        val canDrawOverlay = if (isForceModeEnabled()) {
            permissionsRepository.checkPermission(DrawOverlayPermission).toBoolean()
        } else {
            true
        }

        return canShowNotifications && canWriteSystemSettings && canDrawOverlay
    }

    private suspend fun isForceModeEnabled(): Boolean {
        return settingsRepository.getSetting(Setting.ForcedMode::class).value
    }
}