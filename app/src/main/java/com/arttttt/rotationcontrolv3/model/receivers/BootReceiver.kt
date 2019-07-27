package com.arttttt.rotationcontrolv3.model.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences
import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper
import org.koin.core.KoinComponent

class BootReceiver: BroadcastReceiver(), KoinComponent {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == Intent.ACTION_BOOT_COMPLETED) {
                context?.run {
                    with(getKoin().get<AppPreferences>()) {
                        if (this.getBool(AppPreferences.Companion.Parameters.START_ON_BOOT))
                            with(getKoin().get<ServiceHelper>()) {
                                startService(context)
                            }
                    }
                }
            }
        }
    }
}