package com.arttttt.rotationcontrolv3.utils.servicelauncher

import android.app.ForegroundServiceStartNotAllowedException
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.framework.model.NotificationsPermission
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService
import timber.log.Timber
import javax.inject.Inject

class RotationServiceLauncherImpl @Inject constructor(
    private val context: Context,
    private val permissionsRepository: PermissionsRepository,
) : RotationServiceLauncher {

    private companion object {

        const val BOOT_CHANNEL_ID = "rotation_service_boot"
        const val BOOT_NOTIFICATION_ID = 2
    }

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

    override fun launchOnBoot() {
        /*
         * Starting a foreground service straight from BOOT_COMPLETED is denied on Android 12+
         * (and always for a "specialUse" service on Android 14+). Instead show a notification the
         * user can tap: a foreground service start triggered by a notification tap is exempt.
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            launch()
        } else {
            showBootStartNotification()
        }
    }

    override fun stop() {
        context.stopService(serviceIntent)
    }

    private fun showBootStartNotification() {
        if (!isNotificationPermissionGranted()) return

        val notificationManager = NotificationManagerCompat.from(context)

        notificationManager.createNotificationChannel(
            NotificationChannelCompat
                .Builder(BOOT_CHANNEL_ID, NotificationManagerCompat.IMPORTANCE_DEFAULT)
                .setName(context.getString(R.string.boot_start_channel_name))
                .build()
        )

        val notification = NotificationCompat
            .Builder(context, BOOT_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_rotate)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.boot_start_prompt))
            .setContentIntent(
                PendingIntent.getForegroundService(
                    context,
                    0,
                    serviceIntent,
                    PendingIntent.FLAG_IMMUTABLE,
                )
            )
            .setAutoCancel(true)
            .build()

        notificationManager.notify(BOOT_NOTIFICATION_ID, notification)
    }

    private fun isNotificationPermissionGranted(): Boolean {
        return permissionsRepository.checkPermission(NotificationsPermission) == Permission.Status.Granted
    }
}
