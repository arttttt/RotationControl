package com.arttttt.rotationcontrolv3.device.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.arttttt.rotationcontrolv3.device.services.helper.IRotationServiceHelper
import com.arttttt.rotationcontrolv3.utils.START_ON_BOOT
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.IPreferencesDelegate
import org.koin.core.KoinComponent
import org.koin.core.inject

class BootReceiver: BroadcastReceiver(), KoinComponent {

    private val preferencesDelegate: IPreferencesDelegate by inject()
    private val rotationServiceHelper: IRotationServiceHelper by inject()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        if (
            intent.action == Intent.ACTION_BOOT_COMPLETED &&
            preferencesDelegate.getBool(START_ON_BOOT)
        ) {
            rotationServiceHelper.startRotationService()
        }
    }
}