package com.arttttt.rotationcontrolv3.ui.rotation

import android.app.Notification
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.arkivanov.essenty.instancekeeper.InstanceKeeperDispatcher
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arttttt.rotationcontrolv3.ui.rotation.di.DaggerRotationServiceComponent
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceView
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceViewImpl
import com.arttttt.rotationcontrolv3.utils.extensions.appComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class RotationService : Service() {

    companion object {

        private const val NOTIFICATION_CHANNEL_ID = "rotation_service_notifications"
        private const val NOTIFICATION_CHANNEL_NAME = "Rotation service notifications"

        private const val NOTIFICATION_ID = 1

        private const val FOREGROUND_SERVICE_TYPE_ABSENT = 0

        private val _status = MutableStateFlow(Status.HALTED)
        val status: StateFlow<Status> = _status
    }

    enum class Status {
        RUNNING,
        HALTED;
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
        )
    }

    private val notificationManagerCompat by lazy {
        NotificationManagerCompat.from(applicationContext)
    }

    private val instanceKeeper = InstanceKeeperDispatcher()

    @Inject
    lateinit var controller: RotationServiceController

    override fun onCreate() {
        DaggerRotationServiceComponent
            .factory()
            .create(
                dependencies = applicationContext.appComponent,
                instanceKeeper = instanceKeeper,
            )
            .inject(this)

        super.onCreate()

        controller.platformCallback = RotationServiceController.PlatformCallback { notification ->
            createNotificationChannel()
            showServiceNotification(notification)
        }

        controller.onViewCreated(
            view = view,
            lifecycle = lifecycle,
        )

        lifecycle.resume()
        _status.tryEmit(Status.RUNNING)
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycle.destroy()
        _status.tryEmit(Status.HALTED)
        instanceKeeper.destroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let(view::handleClick)

        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotificationChannel() {
        if (notificationManagerCompat.getNotificationChannel(NOTIFICATION_CHANNEL_ID) != null) return

        val channel = NotificationChannelCompat
            .Builder(
                NOTIFICATION_CHANNEL_ID,
                NotificationManagerCompat.IMPORTANCE_HIGH,
            )
            .setName(NOTIFICATION_CHANNEL_NAME)
            .build()

        notificationManagerCompat.createNotificationChannel(channel)
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