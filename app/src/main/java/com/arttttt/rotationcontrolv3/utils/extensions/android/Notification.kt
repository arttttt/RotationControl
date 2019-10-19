package com.arttttt.rotationcontrolv3.utils.extensions.android

import android.app.Notification
import android.content.Context
import androidx.core.app.NotificationCompat

inline fun notificationOf(context: Context, channelId: String, block: NotificationCompat.Builder.() -> Unit = {}): Notification {
    return NotificationCompat.Builder(context, channelId).apply(block).build()
}