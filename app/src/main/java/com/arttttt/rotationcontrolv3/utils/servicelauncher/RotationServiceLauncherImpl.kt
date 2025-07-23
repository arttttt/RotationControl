package com.arttttt.rotationcontrolv3.utils.servicelauncher

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.framework.model.NotificationsPermission
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService
import javax.inject.Inject

class RotationServiceLauncherImpl @Inject constructor(
    private val context: Context,
    private val permissionsRepository: PermissionsRepository,
) : RotationServiceLauncher {

    private val serviceIntent by lazy {
        Intent(
            context,
            RotationService::class.java,
        )
    }

    override fun launch() {
        if (!isNotificationPermissionGranted()) return

        ContextCompat.startForegroundService(
            context,
            serviceIntent,
        )
    }

    override fun stop() {
        context.stopService(serviceIntent)
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return permissionsRepository.checkPermission(NotificationsPermission) == Permission.Status.Granted
    }
}