package com.arttttt.rotationcontrolv3.device.services.rotation

import android.annotation.SuppressLint
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
import com.arttttt.rotationcontrolv3.device.services.delegate.IWindowDelegate
import com.arttttt.rotationcontrolv3.device.services.rotation.base.BaseService
import com.arttttt.rotationcontrolv3.device.services.rotation.di.rotationServiceModule
import com.arttttt.rotationcontrolv3.device.services.rotation.helper.IRotationServiceHelper
import com.arttttt.rotationcontrolv3.utils.FORCE_MODE
import com.arttttt.rotationcontrolv3.utils.OsUtils
import com.arttttt.rotationcontrolv3.utils.SAVED_ORIENTATION
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.drawoverlays.ICanDrawOverlayChecker
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings.ICanWriteSettingsChecker
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.IPreferencesDelegate
import com.arttttt.rotationcontrolv3.utils.extensions.android.*
import com.arttttt.rotationcontrolv3.utils.extensions.koilin.toInt
import com.arttttt.rotationcontrolv3.utils.extensions.koilin.unsafeCastTo
import com.arttttt.rotationcontrolv3.utils.rxjava.ISchedulersProvider
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.inject

class RotationService: BaseService() {
    @SuppressLint("CheckResult")
    companion object ServiceHelper: IRotationServiceHelper, KoinComponent {
        private val context: Context by inject()
        private val writeSettingsChecker: ICanWriteSettingsChecker by inject()
        private val drawOverlaysChecker: ICanDrawOverlayChecker by inject()
        private val preferencesDelegate: IPreferencesDelegate by inject()
        private val schedulers: ISchedulersProvider by inject()
        private val serviceStatus = BehaviorSubject.createDefault(IRotationServiceHelper.Status.STOPPED)

        override fun getStatusObservable(): Observable<IRotationServiceHelper.Status> = serviceStatus.serialize().share()

        override fun startRotationService() {
            writeSettingsChecker
                .canWriteSettings()
                .filter { canWriteSettings -> canWriteSettings }
                .doOnComplete { toastOf(context, R.string.can_not_write_settings_toast) }
                .map { preferencesDelegate.getBool(FORCE_MODE) }
                .flatMapSingle { isForceModeEnabled ->
                    if (isForceModeEnabled) {
                        drawOverlaysChecker.canDrawOverlay()
                    } else {
                        Single.just(true)
                    }
                }
                .filter { canDrawOverlay -> canDrawOverlay }
                .doOnComplete { toastOf(context, R.string.can_not_draw_overlay_toast) }
                .subscribe { context.startForegroundServiceCompat(intentOf<RotationService>(context)) }
        }

        override fun stopRotationService() {
            stopService<RotationService>(context)
        }

        override fun restartRotationService() {
            if (serviceStatus.value != IRotationServiceHelper.Status.STARTED) return

            serviceStatus
                .filter { status -> status == IRotationServiceHelper.Status.STOPPED }
                .firstElement()
                .subscribe { startRotationService() }

            stopRotationService()
        }

        private const val INTENT_ACTION = "ACTION"
        private const val INTENT_ACTION_CLICK = 2
        private const val INTENT_CLICKED_BUTTON_ID = "BUTTON_ID"

        private const val NOTIFICATION_CHANNEL = "Rotation Control"
        private const val NOTIFICATION_CHANNEL_ID = "rotation_service_notification_channel_id"
        private const val NOTIFICATION_ID = 999
    }

    enum class Orientation(val value: Int) {
        ORIENTATION_PORTRAIT(Surface.ROTATION_0),
        ORIENTATION_LANDSCAPE(Surface.ROTATION_90),
        ORIENTATION_PORTRAIT_REVERSE(Surface.ROTATION_180),
        ORIENTATION_LANDSCAPE_REVERSE(Surface.ROTATION_270),
        ORIENTATION_AUTO(4)
    }

    private val notificationService: NotificationManager by inject()
    private val windowService: WindowManager by inject()

    private var currentOrientation = Orientation.ORIENTATION_AUTO

    private val buttonsIds = arrayOf(
        R.id.btn_auto,
        R.id.btn_portrait,
        R.id.btn_portrait_reverse,
        R.id.btn_landscape,
        R.id.btn_landscape_reverse
    )

    private val windowDelegate: IWindowDelegate by inject()

    override fun onCreate() {
        loadKoinModules(rotationServiceModule)
        super.onCreate()
        initForegroundService()
        serviceStatus.onNext(IRotationServiceHelper.Status.STARTED)
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceStatus.onNext(IRotationServiceHelper.Status.STOPPED)
        windowDelegate.removeWindow()
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
        AccelerometerObserver
            .accelerometerChanges(contentResolver)
            .distinctUntilChanged()
            .flatMapSingle { writeSettingsChecker.canWriteSettings() }
            .filter { canWriteSettings -> canWriteSettings }
            .map { (currentOrientation == Orientation.ORIENTATION_AUTO).toInt() }
            .subscribeUntilDestroy(
                onNext = ::dispatchAccelerometerChanges
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

    private fun dispatchAccelerometerChanges(isEnabled: Int) {
        contentResolver.putInt(Settings.System.ACCELEROMETER_ROTATION, isEnabled)
    }

    private fun createNotification(activeButtonId: Int): Notification {
        val notificationContent = RemoteViews(packageName, R.layout.notification)

        buttonsIds.forEach { buttonId ->
            notificationContent.setOnClickPendingIntent(buttonId, PendingIntent.getService(
                context,
                buttonId,
                intentOf<RotationService>(context) {
                    putExtra(
                        INTENT_ACTION,
                        INTENT_ACTION_CLICK
                    )
                    putExtra(INTENT_CLICKED_BUTTON_ID, buttonId)
                },
                PendingIntent.FLAG_UPDATE_CURRENT)
            )

            configureNotificationButton(notificationContent, buttonId, activeButtonId)
        }

        return notificationOf(this,
            NOTIFICATION_CHANNEL_ID
        ) {
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
        setOrientation(buttonIdToOrientation(buttonId))
            .subscribeUntilDestroy { showNotification(createNotification(buttonId)) }
    }

    private fun setOrientation(newOrientation: Orientation): Completable {
        return writeSettingsChecker
            .canWriteSettings()
            .subscribeOn(schedulers.computation)
            .observeOn(schedulers.ui)
            .filter { canWriteSettings -> canWriteSettings }
            .doOnComplete { stopWithToast(R.string.can_not_write_settings_toast) }
            .flatMapSingle {
                if (preferencesDelegate.getBool(FORCE_MODE)) {
                    drawOverlaysChecker
                        .canDrawOverlay()
                        .filter { canDrawOverlay -> canDrawOverlay }
                        .doOnSuccess { windowDelegate.createOrUpdateWindow(newOrientation) }
                        .doOnComplete { stopWithToast(R.string.can_not_draw_overlay_toast) }
                        .toSingle(false)
                } else {
                    Single.just(true)
                }
            }
            .doOnSuccess {
                currentOrientation = newOrientation
                dispatchAccelerometerChanges((currentOrientation == Orientation.ORIENTATION_AUTO).toInt())
                if (currentOrientation != Orientation.ORIENTATION_AUTO) {
                    contentResolver.putInt(Settings.System.USER_ROTATION, newOrientation.value)
                }
                preferencesDelegate.putInt(SAVED_ORIENTATION, currentOrientation.value)
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

    private fun getInitialRotation(): Orientation {
        return when (preferencesDelegate.getInt(SAVED_ORIENTATION) ?: windowService.defaultDisplay.rotation) {
            Orientation.ORIENTATION_LANDSCAPE.value -> Orientation.ORIENTATION_LANDSCAPE
            Orientation.ORIENTATION_LANDSCAPE_REVERSE.value -> Orientation.ORIENTATION_LANDSCAPE_REVERSE
            Orientation.ORIENTATION_PORTRAIT.value -> Orientation.ORIENTATION_PORTRAIT
            Orientation.ORIENTATION_PORTRAIT_REVERSE.value -> Orientation.ORIENTATION_PORTRAIT_REVERSE
            else -> Orientation.ORIENTATION_AUTO
        }
    }

    private fun buttonIdToOrientation(buttonId: Int): Orientation {
        return when (buttonId) {
            R.id.btn_portrait -> Orientation.ORIENTATION_PORTRAIT
            R.id.btn_portrait_reverse -> Orientation.ORIENTATION_PORTRAIT_REVERSE
            R.id.btn_landscape -> Orientation.ORIENTATION_LANDSCAPE
            R.id.btn_landscape_reverse -> Orientation.ORIENTATION_LANDSCAPE_REVERSE
            else -> Orientation.ORIENTATION_AUTO
        }
    }

    private fun orientationToButtonId(orientation: Orientation): Int {
        return when (orientation) {
            Orientation.ORIENTATION_PORTRAIT -> R.id.btn_portrait
            Orientation.ORIENTATION_PORTRAIT_REVERSE -> R.id.btn_portrait_reverse
            Orientation.ORIENTATION_LANDSCAPE -> R.id.btn_landscape
            Orientation.ORIENTATION_LANDSCAPE_REVERSE -> R.id.btn_landscape_reverse
            else ->  R.id.btn_auto
        }
    }

    private fun stopWithToast(messageRes: Int) {
        toastOf(context, messageRes)
        stopSelf()
    }
}