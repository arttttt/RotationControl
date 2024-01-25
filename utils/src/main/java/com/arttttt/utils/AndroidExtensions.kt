package com.arttttt.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

fun Context.startForegroundServiceCompat(intent: Intent) {
    ContextCompat.startForegroundService(this, intent)
}