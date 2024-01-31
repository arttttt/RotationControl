package com.arttttt.rotationcontrolv3.ui.rotation

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.ServiceCompat
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.domain.entity.OrientationMode
import com.arttttt.rotationcontrolv3.ui.rotation.di.DaggerRotationServiceComponent
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceView
import com.arttttt.rotationcontrolv3.ui.rotation.view.RotationServiceViewImpl
import com.arttttt.rotationcontrolv3.utils.extensions.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

    private var currentOrientationMode: OrientationMode = OrientationMode.Auto
        set(value) {
            field = value

            showServiceNotification()
        }

    private val view: RotationServiceView by lazy {
        RotationServiceViewImpl(
            isButtonActive = { buttonId ->
                OrientationMode.of(buttonId) == currentOrientationMode
            },
            context = applicationContext,
        )
    }

    private val coroutineScope = CoroutineScope(Job())

    @Inject
    lateinit var controller: RotationServiceController

    override fun onCreate() {
        DaggerRotationServiceComponent
            .factory()
            .create(
                dependencies = applicationContext.appComponent,
            )
            .inject(this)

        super.onCreate()

        createNotificationChannel()

        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            view.createNotification(NOTIFICATION_CHANNEL_ID),
            foregroundServiceTypeCompat,
        )

        view
            .events
            .filterIsInstance<RotationServiceView.UiEvent.ButtonEvent>()
            .onEach { event ->
                currentOrientationMode = OrientationMode.of(event)
            }
            .launchIn(coroutineScope)
    }

    override fun onDestroy() {
        super.onDestroy()

        coroutineScope.coroutineContext.cancelChildren()
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

    private fun showServiceNotification() {
        ServiceCompat.startForeground(
            this,
            NOTIFICATION_ID,
            view.createNotification(NOTIFICATION_CHANNEL_ID),
            foregroundServiceTypeCompat,
        )
    }

    private fun OrientationMode.Companion.of(event: RotationServiceView.UiEvent.ButtonEvent): OrientationMode {
        return when (event) {
            RotationServiceView.UiEvent.ButtonEvent.AutoClicked -> OrientationMode.Auto
            RotationServiceView.UiEvent.ButtonEvent.PortraitClicked -> OrientationMode.Portrait
            RotationServiceView.UiEvent.ButtonEvent.PortraitReverseClicked -> OrientationMode.PortraitReverse
            RotationServiceView.UiEvent.ButtonEvent.LandscapeClicked -> OrientationMode.Landscape
            RotationServiceView.UiEvent.ButtonEvent.LandscapeReverseClicked -> OrientationMode.LandscapeReverse
        }
    }

    private fun OrientationMode.Companion.of(buttonId: Int): OrientationMode? {
        return when (buttonId) {
            R.id.btn_auto -> OrientationMode.Auto
            R.id.btn_portrait -> OrientationMode.Portrait
            R.id.btn_portrait_reverse -> OrientationMode.PortraitReverse
            R.id.btn_landscape -> OrientationMode.Landscape
            R.id.btn_landscape_reverse -> OrientationMode.LandscapeReverse
            else -> null
        }
    }
}