package com.arttttt.rotationcontrolv3.ui.rotation.view

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService
import com.arttttt.rotationcontrolv3.utils.extensions.setColorFilter
import kotlinx.coroutines.flow.MutableSharedFlow

class RotationServiceViewImpl(
    private val isButtonActive: (Int) -> Boolean,
    private val context: Context,
) : RotationServiceView {

    companion object {

        private const val NOTIFICATION_BUTTON_CLICKED_ACTION = "notification_button_clicked_action"

        private const val NO_ID = -1
    }

    override val events = MutableSharedFlow<RotationServiceView.UiEvent>(extraBufferCapacity = 1)

    private val buttonsList = listOf(
        R.id.btn_auto,
        R.id.btn_portrait,
        R.id.btn_portrait_reverse,
        R.id.btn_landscape,
        R.id.btn_landscape_reverse,
    )

    override fun createNotification(channelId: String): Notification {
        val remoteViews = createRemoteViews()

        remoteViews.configureButtons(
            context = context,
            isButtonActive = isButtonActive,
        )

        return NotificationCompat
            .Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_rotate)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setCustomContentView(remoteViews)
            .build()
            .apply {
                flags = NotificationCompat.FLAG_ONLY_ALERT_ONCE
            }
    }

    override fun handleClick(intent: Intent) {
        val clickedButtonId = intent
            .getIntExtra(
                NOTIFICATION_BUTTON_CLICKED_ACTION,
                NO_ID,
            )
            .takeIf { it != NO_ID }
            ?: return

        getUiEventFromButtonId(clickedButtonId)?.let(events::tryEmit)
    }

    private fun createRemoteViews(): RemoteViews {
        return RemoteViews(
            context.packageName,
            R.layout.layout_notification,
        )
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
}