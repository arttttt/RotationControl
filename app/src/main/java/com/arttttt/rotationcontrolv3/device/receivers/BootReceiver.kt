package com.arttttt.rotationcontrolv3.device.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.PreferencesDelegate
import com.arttttt.rotationcontrolv3.device.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.utils.START_ON_BOOT
import org.koin.core.KoinComponent
import org.koin.core.get

class BootReceiver: BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (get<PreferencesDelegate>().getBool(START_ON_BOOT)) {
                get<ServiceHelper>().startService(context)
            }
        }
    }
}