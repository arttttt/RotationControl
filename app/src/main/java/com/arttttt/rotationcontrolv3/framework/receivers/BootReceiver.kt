package com.arttttt.rotationcontrolv3.framework.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.arttttt.rotationcontrolv3.ui.rotation.RotationService

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        ContextCompat.startForegroundService(
            context,
            Intent(
                context,
                RotationService::class.java,
            ),
        )
    }
}