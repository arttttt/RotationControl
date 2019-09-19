package com.arttttt.rotationcontrolv3.device.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.PreferencesDelegate
import com.arttttt.rotationcontrolv3.device.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.utils.START_ON_BOOT
import org.koin.core.KoinComponent

class BootReceiver: BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == Intent.ACTION_BOOT_COMPLETED) {
                context?.run {
                    with(getKoin().get<PreferencesDelegate>()) {
                        if (this.getBool(START_ON_BOOT))
                            with(getKoin().get<ServiceHelper>()) {
                                startService(context)
                            }
                    }
                }
            }
        }
    }
}