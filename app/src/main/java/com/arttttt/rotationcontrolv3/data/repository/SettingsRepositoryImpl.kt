package com.arttttt.rotationcontrolv3.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.arttttt.rotationcontrolv3.data.framework.BootReceiver
import com.arttttt.rotationcontrolv3.domain.entity.AppSettings
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class SettingsRepositoryImpl @Inject constructor(
    private val context: Context,
) : SettingsRepository {

    companion object {

        private const val DATASTORE_NAME = "settings"

        private val forcedOrientation = booleanPreferencesKey("forced_orientation")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)

    override suspend fun saveSettings(settings: AppSettings) {
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
            is AppSettings.ForceApplyOrientation -> {
                context.dataStore.edit { dataStore ->
                    dataStore[forcedOrientation] = settings.value
                }
            }
        }
    }

    override suspend fun getSettings(): List<AppSettings> {
        return listOf(
            AppSettings.StartOnBoot(
                value = context
                    .packageManager
                    .getComponentEnabledSetting(ComponentName(context, BootReceiver::class.java)) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
            ),
            AppSettings.ForceApplyOrientation(
                value = context.dataStore.data.first()[forcedOrientation] ?: false,
            )
        )
    }
}