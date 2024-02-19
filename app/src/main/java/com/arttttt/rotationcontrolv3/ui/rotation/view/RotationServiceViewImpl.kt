package com.arttttt.rotationcontrolv3.ui.rotation.view

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import com.arkivanov.mvikotlin.core.utils.diff
import com.arttttt.rotationcontrolv3.MainActivity
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService
import com.arttttt.rotationcontrolv3.ui.rotation.model.NotificationButton
import com.arttttt.rotationcontrolv3.utils.extensions.setColorFilter
import kotlinx.coroutines.flow.MutableSharedFlow

class RotationServiceViewImpl(
    private val context: Context,
    private val channelId: String,
) : RotationServiceView {

    companion object {

        private const val NOTIFICATION_BUTTON_CLICKED_ACTION = "notification_button_clicked_action"
        private const val STOP_SERVICE_ACTION = "stop_service_action"

        private const val LAUNCH_PAYLOAD = "launch_payload"

        private const val NO_ID = -1
    }

    private val activeRenderer = diff {
        diff(
            get = RotationServiceView.State.Active::selectedButton,
            set = this@RotationServiceViewImpl::handleActiveButtonChanged,
        )
    }

    private val errorRenderer = diff<RotationServiceView.State.Error> {
        diff(
            get = { it },
            set = { handleErrorState() },
        )
    }

    override val events = MutableSharedFlow<RotationServiceView.UiEvent>(extraBufferCapacity = 1)

    private val buttonsList = listOf(
        R.id.btn_auto,
        R.id.btn_portrait,
        R.id.btn_portrait_reverse,
        R.id.btn_landscape,
        R.id.btn_landscape_reverse,
    )

    override fun render(model: RotationServiceView.State) {
        when (model) {
            is RotationServiceView.State.Active -> activeRenderer.render(model)
            is RotationServiceView.State.Error -> errorRenderer.render(model)
        }
    }

    override fun handleClick(intent: Intent) {
        when (intent.action) {
            NOTIFICATION_BUTTON_CLICKED_ACTION -> handleButtonClicked(intent)
            STOP_SERVICE_ACTION -> {
                events.tryEmit(
                    RotationServiceView.UiEvent.StopServiceClicked(
                        payload = IntentCompat.getParcelableExtra(
                            intent,
                            LAUNCH_PAYLOAD,
                            Intent::class.java,
                        )
                    )
                )
            }
        }
    }

    private fun handleButtonClicked(intent: Intent) {
        val clickedButtonId = intent
            .getIntExtra(
                NOTIFICATION_BUTTON_CLICKED_ACTION,
                NO_ID,
            )
            .takeIf { it != NO_ID }
            ?: return

        getUiEventFromButtonId(clickedButtonId)?.let(events::tryEmit)
    }

    private fun handleActiveButtonChanged(activeButton: NotificationButton) {
        val remoteViews = createRemoteViews()

        remoteViews.configureButtons(
            context = context,
            isButtonActive = { id -> NotificationButton.of(id) == activeButton },
        )

        events.tryEmit(
            RotationServiceView.UiEvent.NotificationUpdated(
                notification = NotificationCompat
                    .Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_rotate)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(remoteViews)
                    .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
                    .addAction(
                        NotificationCompat.Action
                            .Builder(
                                null,
                                context.getString(R.string.stop_service),
                                PendingIntent.getService(
                                    context,
                                    0,
                                    Intent(context, RotationService::class.java).apply {
                                        action = STOP_SERVICE_ACTION
                                    },
                                    PendingIntent.FLAG_IMMUTABLE,
                                ),
                            )
                            .build()
                    )
                    .build()
                    .apply {
                        flags = NotificationCompat.FLAG_ONLY_ALERT_ONCE
                    }
            )
        )
    }

    @SuppressLint("LaunchActivityFromNotification")
    private fun handleErrorState() {
        events.tryEmit(
            RotationServiceView.UiEvent.NotificationUpdated(
                notification = NotificationCompat
                    .Builder(context, channelId)
                    .setSmallIcon(R.drawable.ic_rotate)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentText("Permissions not granted")
                    .setContentIntent(
                        PendingIntent.getService(
                            context,
                            0,
                            Intent(context, RotationService::class.java).apply {
                                action = STOP_SERVICE_ACTION

                                putExtra(
                                    LAUNCH_PAYLOAD,
                                    Intent(context, MainActivity::class.java).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    },
                                )
                            },
                            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
                        ),
                    )
                    .build()
            )
        )
    }

    private fun createRemoteViews(): RemoteViews {
        return RemoteViews(
            context.packageName,
            R.layout.layout_notification,
        )
    }

    private fun getUiEventFromButtonId(id: Int): RotationServiceView.UiEvent? {
        return when (id) {
            R.id.btn_auto -> RotationServiceView.UiEvent.ButtonEvent.AutoClicked
            R.id.btn_portrait -> RotationServiceView.UiEvent.ButtonEvent.PortraitClicked
            R.id.btn_portrait_reverse -> RotationServiceView.UiEvent.ButtonEvent.PortraitReverseClicked
            R.id.btn_landscape -> RotationServiceView.UiEvent.ButtonEvent.LandscapeClicked
            R.id.btn_landscape_reverse -> RotationServiceView.UiEvent.ButtonEvent.LandscapeReverseClicked
            else -> null
        }
    }

    private fun RemoteViews.configureButtons(
        context: Context,
        isButtonActive: (Int) -> Boolean,
    ) {
        buttonsList.forEach { buttonId ->
            this.setOnClickPendingIntent(
                buttonId,
                PendingIntent.getService(
                    context,
                    buttonId,
                    Intent(
                        context,
                        RotationService::class.java
                    ).apply {
                        action = NOTIFICATION_BUTTON_CLICKED_ACTION

                        putExtra(
                            NOTIFICATION_BUTTON_CLICKED_ACTION,
                            buttonId,
                        )
                    },
                    PendingIntent.FLAG_IMMUTABLE,
                )
            )

            this.configureNotificationButton(
                context = context,
                buttonId = buttonId,
                isActive = isButtonActive.invoke(buttonId),
            )
        }
    }

    private fun RemoteViews.configureNotificationButton(
        context: Context,
        buttonId: Int,
        isActive: Boolean,
    ) {
        this.setColorFilter(
            buttonId,
            ContextCompat.getColor(
                context,
                if (isActive) {
                    R.color.colorActiveButtonTint
                } else {
                    R.color.colorInactiveButtonTint
                }
            )
        )
    }

    private fun NotificationButton.Companion.of(value: Int): NotificationButton? {
        return when (value) {
            R.id.btn_auto -> NotificationButton.Auto
            R.id.btn_portrait -> NotificationButton.Portrait
            R.id.btn_portrait_reverse -> NotificationButton.PortraitReverse
            R.id.btn_landscape -> NotificationButton.Landscape
            R.id.btn_landscape_reverse -> NotificationButton.LandscapeReverse
            else -> null
        }
    }
}