package com.arttttt.rotationcontrolv3.device.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings.System
import com.arttttt.rotationcontrolv3.device.services.base.ServiceHelper
import android.view.Surface
import com.arttttt.rotationcontrolv3.R
import android.widget.RemoteViews
import android.app.PendingIntent
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.res.Configuration
import android.database.ContentObserver
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.IPermissionsCheckerDelegate
import com.arttttt.rotationcontrolv3.utils.OsUtils
import org.koin.core.KoinComponent
import org.koin.core.inject


class RotationService: Service() {
    override fun onBind(intent: Intent?) = null

    companion object: ServiceHelper, KoinComponent {
        private val mStarted = MutableLiveData<Boolean>()
        private val permissionsChecker: IPermissionsCheckerDelegate by inject()

        init {
            mStarted.value = false
        }

        override fun isStarted(): LiveData<Boolean> = mStarted

        override fun startService(context: Context) {
            if (!permissionsChecker.canWriteSettings()) {
                Toast.makeText(context, R.string.can_not_write_settings_toast, Toast.LENGTH_LONG).show()

                return
            }

            Intent(context, RotationService::class.java).let { intent ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(intent)
                } else {
                    context.startService(intent)
                }
            }
        }

        override fun stopService(context: Context) {
            Intent(context, RotationService::class.java).let {intent ->
                context.stopService(intent)
            }
        }
    }

    private val intentAction = "ACTION"
    private val intentActionClick = 2
    private val intentClickedButtonId = "BUTTON_ID"

    private val notificationChannel = "Rotation Control"
    private val notificationChannelId = "rotation_service_notification_channel_id"
    private val notificationId = 999

    private val orientationPortrait = Surface.ROTATION_0
    private val orientationLandscape = Surface.ROTATION_90
    private val orientationPortraitReverse = Surface.ROTATION_180
    private val orientationLandscapeReverse = Surface.ROTATION_270
    private val orientationAuto = 4

    private val accelerometerObserver = object: ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            checkCurrentMode()
        }
    }

    private var mScreenOrientation = orientationAuto

    private val buttonsIds = arrayOf(
        R.id.btn_auto,
        R.id.btn_portrait,
        R.id.btn_portrait_reverse,
        R.id.btn_landscape,
        R.id.btn_landscape_reverse)

    override fun onCreate() {
        super.onCreate()

        mStarted.value = true

        contentResolver.registerContentObserver(System.getUriFor(System.ACCELEROMETER_ROTATION),
            false,
            accelerometerObserver)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                notificationChannel,
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(channel)
        }

        mScreenOrientation = convertOrientation(resources.configuration.orientation)
        setOrientation()

        showNotification(createNotification())
    }

    override fun onDestroy() {
        super.onDestroy()

        mStarted.value = false
        contentResolver.unregisterContentObserver(accelerometerObserver)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when(intent.getIntExtra(intentAction, 0)) {
                intentActionClick -> { handleNotificationClick(intent.getIntExtra(intentClickedButtonId, 0)) }
            }
        }
        return START_STICKY
    }

    private fun checkCurrentMode() {
        if (!permissionsChecker.canWriteSettings() || mScreenOrientation == orientationAuto)
            return

        System.putInt(contentResolver, System.ACCELEROMETER_ROTATION, 0)
    }

    private fun convertOrientation(origOrientation: Int): Int {
        return if (origOrientation == Configuration.ORIENTATION_PORTRAIT)
            orientationPortrait
        else
            orientationLandscape
    }

    private fun createNotification(): Notification {
        val notificationContent = RemoteViews(packageName, R.layout.notification)

        val activeButtonId = when (mScreenOrientation) {
            orientationPortrait -> R.id.btn_portrait
            orientationPortraitReverse -> R.id.btn_portrait_reverse
            orientationLandscape -> R.id.btn_landscape
            orientationLandscapeReverse -> R.id.btn_landscape_reverse
            else ->  R.id.btn_auto
        }

        for (buttonId in buttonsIds) {
            val intent = Intent(this, RotationService::class.java).apply {
                putExtra(intentAction, intentActionClick)
                putExtra(intentClickedButtonId, buttonId)
            }
            notificationContent.setOnClickPendingIntent(buttonId, PendingIntent.getService(this,
                buttonId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT))

            if (buttonId == activeButtonId) {
                notificationContent.setInt(buttonId,
                    "setBackgroundResource",
                    R.drawable.active_button_background)

                val colorRes = if (OsUtils.isMiui()) R.color.colorActiveButtonTintMiui else R.color.colorActiveButtonTint

                notificationContent.setInt(buttonId,
                    "setColorFilter",
                    ContextCompat.getColor(this, colorRes))
            } else {
                notificationContent.setInt(buttonId,
                    "setBackgroundResource",
                    android.R.color.transparent)

                val colorRes = if (OsUtils.isMiui()) R.color.colorInactiveButtonTintMiui else R.color.colorInactiveButtonTint

                notificationContent.setInt(buttonId,
                    "setColorFilter",
                    ContextCompat.getColor(this, colorRes))
            }
        }

        return NotificationCompat.Builder(this, notificationChannelId)
            .setCustomContentView(notificationContent)
            .setSmallIcon(R.drawable.ic_rotate)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    setStyle(NotificationCompat.DecoratedCustomViewStyle())
            }
            .build()
            .apply {
                flags = NotificationCompat.FLAG_ONLY_ALERT_ONCE
            }
    }

    private fun handleNotificationClick(buttonId: Int) {
        if (!permissionsChecker.canWriteSettings()) {
            Toast.makeText(this, R.string.can_not_write_settings_toast, Toast.LENGTH_LONG).show()

            stopSelf()
            return
        }

        mScreenOrientation =  when (buttonId) {
            R.id.btn_portrait -> orientationPortrait
            R.id.btn_portrait_reverse -> orientationPortraitReverse
            R.id.btn_landscape -> orientationLandscape
            R.id.btn_landscape_reverse -> orientationLandscapeReverse
            else -> orientationAuto
        }
        setOrientation()
        showNotification(createNotification())
    }

    private fun showNotification(notification: Notification) = startForeground(notificationId, notification)

    private fun setOrientation() {
        if (!permissionsChecker.canWriteSettings())
            return

        if (mScreenOrientation == orientationAuto)
            System.putInt(contentResolver, System.ACCELEROMETER_ROTATION, 1)
        else {
            checkCurrentMode()
            System.putInt(contentResolver, System.USER_ROTATION, mScreenOrientation)
        }
    }
}