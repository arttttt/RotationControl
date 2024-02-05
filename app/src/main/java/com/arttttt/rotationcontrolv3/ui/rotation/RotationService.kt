package com.arttttt.rotationcontrolv3.ui.rotation

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arttttt.permissions.utils.extensions.toBoolean
import com.arttttt.rotationcontrolv3.data.model.DrawOverlayPermission
import com.arttttt.rotationcontrolv3.data.model.NotificationsPermission
import com.arttttt.rotationcontrolv3.data.model.WriteSettingsPermission
import com.arttttt.rotationcontrolv3.domain.entity.Setting
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.ui.rotation.di.DaggerRotationServiceComponent
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceView
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceViewImpl
import com.arttttt.rotationcontrolv3.utils.extensions.appComponent
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RotationService : Service() {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "rotation_service_notifications"
        private const val NOTIFICATION_CHANNEL_NAME = "Rotation service notifications"

        private const val NOTIFICATION_ID = 1

        private const val FOREGROUND_SERVICE_TYPE_ABSENT = 0
    }

    @get:Suppress("DEPRECATION")
    private val foregroundServiceTypeCompat: Int
        get() {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
                else -> FOREGROUND_SERVICE_TYPE_ABSENT
            }
        }

    private val lifecycle = LifecycleRegistry()

    private val view: RotationServiceView by lazy {
        RotationServiceViewImpl(
            context = applicationContext,
            channelId = NOTIFICATION_CHANNEL_ID,
            onNotificationUpdated = this::showServiceNotification,
        )
    }

    @Inject
    lateinit var controller: RotationServiceController

    @Inject
    lateinit var permissionsRepository: PermissionsRepository

    @Inject
    lateinit var settingsRepository: SettingsRepository

    override fun onCreate() {
        DaggerRotationServiceComponent
            .factory()
            .create(
                dependencies = applicationContext.appComponent,
            )
            .inject(this)

        super.onCreate()

        val canLaunchService = runBlocking {
            val canShowNotifications = permissionsRepository.checkPermission(NotificationsPermission).toBoolean()
            val canWriteSystemSettings = permissionsRepository.checkPermission(WriteSettingsPermission).toBoolean()
            val canDrawOverlay = if (settingsRepository.getSetting(Setting.ForcedMode::class).value) {
                permissionsRepository.checkPermission(DrawOverlayPermission).toBoolean()
            } else {
                true
            }

            canShowNotifications && canWriteSystemSettings && canDrawOverlay
        }

        if (!canLaunchService) {
            createNotificationChannel()

            controller.onViewCreated(
                view = view,
                lifecycle = lifecycle,
            )

            lifecycle.resume()
        } else {
            ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)

            Toast.makeText(applicationContext, "permissions are not granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycle.destroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent ?: return START_STICKY

        view.handleClick(intent)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannelCompat
            .Builder(
                NOTIFICATION_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_HIGH,
            )
            .setName(NOTIFICATION_CHANNEL_NAME)
            .build()

        NotificationManagerCompat
            .from(applicationContext)
            .createNotificationChannel(channel)
    }

    private fun showServiceNotification(notification: Notification) {
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            notification,
            foregroundServiceTypeCompat,
        )
    }
}