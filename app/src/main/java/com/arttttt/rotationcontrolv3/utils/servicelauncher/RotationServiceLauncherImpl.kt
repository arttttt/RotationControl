package com.arttttt.rotationcontrolv3.utils.servicelauncher

import android.app.ForegroundServiceStartNotAllowedException
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.framework.model.NotificationsPermission
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService
import timber.log.Timber
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

        try {
            ContextCompat.startForegroundService(
                context,
                serviceIntent,
            )
        } catch (e: Exception) {
            /*
             * Android 12+ can deny starting a foreground service from the background, and on
             * Android 14+ a BOOT_COMPLETED receiver isn't allowed to start a "specialUse"
             * foreground service at all. There is no way to force it, so just don't crash.
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && e is ForegroundServiceStartNotAllowedException) {
                Timber.e(e, "Not allowed to start the rotation service from the background")
            } else {
                throw e
            }
        }
    }

    override fun stop() {
        context.stopService(serviceIntent)
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return permissionsRepository.checkPermission(NotificationsPermission) == Permission.Status.Granted
    }
}