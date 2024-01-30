package com.arttttt.rotationcontrolv3.ui.rotation

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.entity.OrientationMode
import com.arttttt.rotationcontrolv3.utils.extensions.setColorFilter

class RotationService : Service() {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "rotation_service_notifications"
        private const val NOTIFICATION_CHANNEL_NAME = "Rotation service notifications"

        private const val NOTIFICATION_ID = 1

        private const val FOREGROUND_SERVICE_TYPE_ABSENT = 0

        private const val NOTIFICATION_BUTTON_CLICKED_ACTION = "notification_button_clicked_action"
    }

    private val buttonsMap = mapOf(
        R.id.btn_auto to OrientationMode.Auto,
        R.id.btn_portrait to OrientationMode.Portrait,
        R.id.btn_portrait_reverse to OrientationMode.PortraitReverse,
        R.id.btn_landscape to OrientationMode.Landscape,
        R.id.btn_landscape_reverse to OrientationMode.LandscapeReverse,
    )

    @get:Suppress("DEPRECATION")
    private val foregroundServiceTypeCompat: Int
        get() {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q -> ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE
                else -> FOREGROUND_SERVICE_TYPE_ABSENT
            }
        }

    private var currentOrientationMode: OrientationMode = OrientationMode.Auto

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()

        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            createNotification(),
            foregroundServiceTypeCompat,
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent ?: return START_STICKY

        val orientationMode = IntentCompat.getParcelableExtra(
            intent,
            NOTIFICATION_BUTTON_CLICKED_ACTION,
            OrientationMode::class.java,
        )

        orientationMode ?: return START_STICKY

        currentOrientationMode = orientationMode

        startForeground(
            NOTIFICATION_ID,
            createNotification(),
        )

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        val notificationContent = RemoteViews(packageName, R.layout.layout_notification)

        buttonsMap.forEach { (buttonId, orientation) ->
            notificationContent.setOnClickPendingIntent(
                buttonId,
                PendingIntent.getService(
                    applicationContext,
                    buttonId,
                    Intent(applicationContext, RotationService::class.java).apply {
                        putExtra(
                            NOTIFICATION_BUTTON_CLICKED_ACTION,
                            orientation
                        )
                    },
                    PendingIntent.FLAG_IMMUTABLE,
                )
            )

            notificationContent.configureNotificationButton(
                buttonId = buttonId,
                isActive = currentOrientationMode == orientation,
            )
        }

        return NotificationCompat
            .Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_rotate)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(notificationContent)
            .build()
            .apply {
                flags = NotificationCompat.FLAG_ONLY_ALERT_ONCE
            }
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

    private fun RemoteViews.configureNotificationButton(
        buttonId: Int,
        isActive: Boolean,
    ) {
        this.setColorFilter(
            buttonId,
            ContextCompat.getColor(
                applicationContext,
                if (isActive) {
                    R.color.colorActiveButtonTint
                } else {
                    R.color.colorInactiveButtonTint
                }
            )
        )
    }
}