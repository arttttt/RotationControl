package com.arttttt.rotationcontrolv3.model.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences
import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper
import org.koin.standalone.StandAloneContext

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action == Intent.ACTION_BOOT_COMPLETED) {
                context?.run {
                    val koinContext = StandAloneContext.getKoin().koinContext
                    with(koinContext.get<AppPreferences>()) {
                        if (this.getBool(AppPreferences.Companion.Parameters.START_ON_BOOT))
                            with(koinContext.get<ServiceHelper>()) {
                                startService(context)
                            }
                    }
                }
            }
        }
    }
}