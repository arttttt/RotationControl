@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.arttttt.rotationcontrolv3.device.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.view.Surface
import android.view.WindowManager
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.device.services.base.BaseService
import com.arttttt.rotationcontrolv3.device.services.di.rotationServiceModule
import com.arttttt.rotationcontrolv3.device.services.helper.IRotationServiceHelper
import com.arttttt.rotationcontrolv3.utils.OsUtils
import com.arttttt.rotationcontrolv3.utils.SAVED_ORIENTATION
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.ICanWriteSettingsChecker
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.IPreferencesDelegate
import com.arttttt.rotationcontrolv3.utils.extensions.android.*
import com.arttttt.rotationcontrolv3.utils.extensions.koilin.unsafeCastTo
import com.arttttt.rotationcontrolv3.utils.rxjava.ISchedulersProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.inject

class RotationService: BaseService() {
    companion object ServiceHelper: IRotationServiceHelper, KoinComponent {
        private val context: Context by inject()
        private val permissionsChecker: ICanWriteSettingsChecker by inject()
        private val schedulers: ISchedulersProvider by inject()
        private val serviceStatus = BehaviorSubject.createDefault(IRotationServiceHelper.Status.STOPPED)

        override fun getStatusObservable(): Observable<IRotationServiceHelper.Status> = serviceStatus.serialize().share()

        override fun startRotationService() {
            permissionsChecker
                .canWriteSettings()
                .subscribeOn(schedulers.computation)
                .observeOn(schedulers.ui)
                .subscribeUntilDestroy { canWriteSettings -> dispatchCanWriteSettings(canWriteSettings) }
        }

        override fun stopRotationService() {
            stopService<RotationService>(context)
        }

        private fun dispatchCanWriteSettings(canWriteSettings: Boolean) {
            if (canWriteSettings) {
                context.startForegroundServiceCompat(intentOf<RotationService>(context))
            } else {
                toastOf(context, R.string.can_not_write_settings_toast)
            }
        }

        private const val INTENT_ACTION = "ACTION"
        private const val INTENT_ACTION_CLICK = 2
        private const val INTENT_CLICKED_BUTTON_ID = "BUTTON_ID"

        private const val NOTIFICATION_CHANNEL = "Rotation Control"
        private const val NOTIFICATION_CHANNEL_ID = "rotation_service_notification_channel_id"
        private const val NOTIFICATION_ID = 999

        private const val ORIENTATION_PORTRAIT = Surface.ROTATION_0
        private const val ORIENTATION_LANDSCAPE = Surface.ROTATION_90
        private const val ORIENTATION_PORTRAIT_REVERSE = Surface.ROTATION_180
        private const val ORIENTATION_LANDSCAPE_REVERSE = Surface.ROTATION_270
        private const val ORIENTATION_AUTO = 4
    }

    private val notificationService: NotificationManager by lazy { getSystemService(Context.NOTIFICATION_SERVICE).unsafeCastTo<NotificationManager>() }
    private val windowService: WindowManager by lazy { getSystemService(Context.WINDOW_SERVICE).unsafeCastTo<WindowManager>() }

    private var currentOrientation = ORIENTATION_AUTO

    private val buttonsIds = arrayOf(
        R.id.btn_auto,
        R.id.btn_portrait,
        R.id.btn_portrait_reverse,
        R.id.btn_landscape,
        R.id.btn_landscape_reverse
    )

    private val accelerometerObserver: AccelerometerObserver by inject()
    private val preferencesDelegate: IPreferencesDelegate by inject()

    override fun onCreate() {
        loadKoinModules(rotationServiceModule)
        super.onCreate()
        initForegroundService()
        serviceStatus.onNext(IRotationServiceHelper.Status.STARTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceStatus.onNext(IRotationServiceHelper.Status.STOPPED)
        unloadKoinModules(rotationServiceModule)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent == null) return START_STICKY

        val action by intent.extra(INTENT_ACTION, 0)

        when(action) {
            INTENT_ACTION_CLICK -> {
                val buttonId by intent.extra(INTENT_CLICKED_BUTTON_ID, 0)
                handleNotificationClick(buttonId)
            }
        }

        return START_STICKY
    }

    private fun initForegroundService() {
        accelerometerObserver
            .getAccelerometerObservable()
            .subscribeOn(schedulers.computation)
            .observeOn(schedulers.computation)
            .flatMapSingle { permissionsChecker.canWriteSettings() }
            .filter { canWriteSettings -> canWriteSettings }
            .filter { currentOrientation != ORIENTATION_AUTO }
            .subscribeUntilDestroy { dispatchAccelerometerChanges() }

        contentResolver.registerContentObserver(
            Settings.System.ACCELEROMETER_ROTATION,
            false,
            accelerometerObserver
        )

        setOrientation(getInitialRotation())
            .subscribeOn(schedulers.computation)
            .observeOn(schedulers.ui)
            .subscribeUntilDestroy {
                createNotificationChannelIfNeeded()

                showNotification(
                    createNotification(
                        orientationToButtonId(currentOrientation)
                    )
                )
            }
    }

    private fun dispatchAccelerometerChanges() {
        contentResolver.putInt(Settings.System.ACCELEROMETER_ROTATION, 0)
    }

    private fun createNotification(activeButtonId: Int): Notification {
        val notificationContent = RemoteViews(packageName, R.layout.notification)

        buttonsIds.forEach { buttonId ->
            notificationContent.setOnClickPendingIntent(buttonId, PendingIntent.getService(
                context,
                buttonId,
                intentOf<RotationService>(context) {
                    putExtra(INTENT_ACTION, INTENT_ACTION_CLICK)
                    putExtra(INTENT_CLICKED_BUTTON_ID, buttonId)
                },
                PendingIntent.FLAG_UPDATE_CURRENT)
            )

            configureNotificationButton(notificationContent, buttonId, activeButtonId)
        }

        return notificationOf(this, NOTIFICATION_CHANNEL_ID) {
            setCustomContentView(notificationContent)
            setSmallIcon(R.drawable.ic_rotate)
            priority = NotificationCompat.PRIORITY_MAX
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                setStyle(NotificationCompat.DecoratedCustomViewStyle())
            }
        }.apply { flags = NotificationCompat.FLAG_ONLY_ALERT_ONCE }
    }

    private fun configureNotificationButton(notificationContent: RemoteViews, buttonId: Int, activeButtonId: Int) {
        notificationContent.setBackgroundResource(
            buttonId,
            if (buttonId == activeButtonId) {
                R.drawable.active_button_background
            } else {
                android.R.color.transparent
            }
        )

        notificationContent.setColorFilter(
            buttonId,
            context.getColorCompat(
                if (buttonId == activeButtonId) {
                    if (OsUtils.isMiui())
                        R.color.colorActiveButtonTintMiui
                    else
                        R.color.colorActiveButtonTint
                } else {
                    if (OsUtils.isMiui())
                        R.color.colorInactiveButtonTintMiui
                    else
                        R.color.colorInactiveButtonTint
                }
            )
        )
    }

    private fun handleNotificationClick(buttonId: Int) {
        permissionsChecker
            .canWriteSettings()
            .subscribeOn(schedulers.computation)
            .subscribeOn(schedulers.computation)
            .filter { canWriteSettings -> canWriteSettings }
            .flatMapSingleElement { setOrientation(buttonIdToOrientation(buttonId)).toSingleDefault(Unit) }
            .subscribeUntilDestroy(
                onComplete = {
                    toastOf(context, R.string.can_not_write_settings_toast)

                    stopSelf()
                },
                onNext =  { showNotification(createNotification(orientationToButtonId(currentOrientation))) }
            )
    }

    private fun setOrientation(newOrientation: Int): Completable {
        return permissionsChecker
            .canWriteSettings()
            .subscribeOn(schedulers.computation)
            .observeOn(schedulers.computation)
            .filter { canWriteSettings -> canWriteSettings }
            .doOnSuccess {
                if (newOrientation == ORIENTATION_AUTO) {
                    contentResolver.putInt(Settings.System.ACCELEROMETER_ROTATION, 1)
                } else {
                    dispatchAccelerometerChanges()
                    contentResolver.putInt(Settings.System.USER_ROTATION, newOrientation)
                }
                currentOrientation = newOrientation
                preferencesDelegate.putInt(SAVED_ORIENTATION, currentOrientation)
            }
            .ignoreElement()
    }

    private fun showNotification(notification: Notification) {
        startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannelIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationService.createNotificationChannel(
                NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL,
                    NotificationManager.IMPORTANCE_HIGH
                )
            )
        }
    }

    private fun getInitialRotation(): Int {
        return preferencesDelegate.getInt(SAVED_ORIENTATION) ?: windowService.defaultDisplay.rotation
    }

    private fun buttonIdToOrientation(buttonId: Int): Int {
        return when (buttonId) {
            R.id.btn_portrait -> ORIENTATION_PORTRAIT
            R.id.btn_portrait_reverse -> ORIENTATION_PORTRAIT_REVERSE
            R.id.btn_landscape -> ORIENTATION_LANDSCAPE
            R.id.btn_landscape_reverse -> ORIENTATION_LANDSCAPE_REVERSE
            else -> ORIENTATION_AUTO
        }
    }

    private fun orientationToButtonId(orientation: Int): Int {
        return when (orientation) {
            ORIENTATION_PORTRAIT -> R.id.btn_portrait
            ORIENTATION_PORTRAIT_REVERSE -> R.id.btn_portrait_reverse
            ORIENTATION_LANDSCAPE -> R.id.btn_landscape
            ORIENTATION_LANDSCAPE_REVERSE -> R.id.btn_landscape_reverse
            else ->  R.id.btn_auto
        }
    }
}