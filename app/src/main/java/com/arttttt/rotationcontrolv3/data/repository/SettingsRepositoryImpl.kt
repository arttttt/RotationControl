package com.arttttt.rotationcontrolv3.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.arttttt.rotationcontrolv3.data.framework.receivers.BootReceiver
import com.arttttt.rotationcontrolv3.domain.entity.settings.Setting
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.utils.unsafeCastTo
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import kotlin.reflect.KClass


class SettingsRepositoryImpl @Inject constructor(
    private val context: Context,
) : SettingsRepository {

    companion object {

        private const val DATASTORE_NAME = "settings"

        private val forcedOrientation = booleanPreferencesKey("forced_orientation")
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(DATASTORE_NAME)
    override suspend fun <T> getSetting(clazz: KClass<out Setting<T>>): Setting<T> {
        val result = when (clazz) {
            Setting.StartOnBoot::class -> {
                Setting.StartOnBoot(
                    value = context
                        .packageManager
                        .getComponentEnabledSetting(
                            ComponentName(
                                context,
                                BootReceiver::class.java
                            )
                        ) == PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                )
            }
            Setting.ForcedMode::class -> {
                Setting.ForcedMode(
                    value = context.dataStore.data.first()[forcedOrientation] ?: false,
                )
            }
            else -> throw IllegalArgumentException("unsupported setting")
        }

        return result.unsafeCastTo()
    }

    override suspend fun <T> saveSetting(clazz: KClass<out Setting<T>>, value: T) {
        when (clazz) {
            Setting.StartOnBoot::class -> {
                context
                    .packageManager
                    .setComponentEnabledSetting(
                        ComponentName(context, BootReceiver::class.java),
                        if (value as Boolean) {
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        } else {
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                        },
                        PackageManager.DONT_KILL_APP,
                    )
            }

            Setting.ForcedMode::class -> {
                context.dataStore.edit { dataStore ->
                    dataStore[forcedOrientation] = value as Boolean
                }
            }
        }
    }

    override suspend fun getAllSettings(): List<Setting<*>> {
        return listOf(
            getSetting(Setting.StartOnBoot::class),
            getSetting(Setting.ForcedMode::class),
        )
    }
}