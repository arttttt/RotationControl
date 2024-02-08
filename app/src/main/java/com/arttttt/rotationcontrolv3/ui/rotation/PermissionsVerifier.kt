package com.arttttt.rotationcontrolv3.ui.rotation

import com.arttttt.permissions.utils.extensions.toBoolean
import com.arttttt.rotationcontrolv3.data.model.DrawOverlayPermission
import com.arttttt.rotationcontrolv3.data.model.NotificationsPermission
import com.arttttt.rotationcontrolv3.data.model.WriteSettingsPermission
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository

class PermissionsVerifier(
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
}