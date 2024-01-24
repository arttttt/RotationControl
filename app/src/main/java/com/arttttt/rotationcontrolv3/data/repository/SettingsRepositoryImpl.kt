package com.arttttt.rotationcontrolv3.data.repository

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import com.arttttt.rotationcontrolv3.device.receivers.BootReceiver
import com.arttttt.rotationcontrolv3.domain.entity.AppSettings
import com.arttttt.rotationcontrolv3.domain.entity.Settings
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import javax.inject.Inject


class SettingsRepositoryImpl @Inject constructor(
    private val context: Context,
) : SettingsRepository {

    override fun saveSettings(settings: AppSettings) {
        when (settings) {
            is AppSettings.StartOnBoot -> {
                context
                    .packageManager
                    .setComponentEnabledSetting(
                        ComponentName(context, BootReceiver::class.java),
                        if (settings.value) {
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        } else {
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                        },
                        PackageManager.DONT_KILL_APP
                    )
            }
        }
    }

    override fun getSettings(): List<AppSettings> {
        return listOf(
            AppSettings.StartOnBoot(
                settings = Settings.START_ON_BOOT,
                value = context
                    .packageManager
                    .getComponentEnabledSetting(ComponentName(context, BootReceiver::class.java)) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            )
        )
    }
}