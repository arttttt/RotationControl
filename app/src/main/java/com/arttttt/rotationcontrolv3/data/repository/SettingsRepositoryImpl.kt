package com.arttttt.rotationcontrolv3.data.repository

import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.arttttt.rotationcontrolv3.domain.entity.settings.SettingKey
import com.arttttt.rotationcontrolv3.domain.entity.settings.SettingValue
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.framework.receivers.BootReceiver
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

    override suspend fun <T> get(key: SettingKey<T>): T {
        @Suppress("UNCHECKED_CAST")
        return when (key) {
            SettingKey.StartOnBoot -> isComponentEnabled(BootReceiver::class)
            SettingKey.ForcedMode -> readBoolean(forcedOrientation, SettingKey.ForcedMode.default)
        } as T
    }

    override suspend fun <T> save(key: SettingKey<T>, value: T) {
        when (key) {
            SettingKey.StartOnBoot -> setComponentEnabled(BootReceiver::class, value as Boolean)
            SettingKey.ForcedMode -> writeBoolean(forcedOrientation, value as Boolean)
        }
    }

    override suspend fun getAll(): List<SettingValue<*>> {
        return listOf(
            SettingValue(SettingKey.StartOnBoot, get(SettingKey.StartOnBoot)),
            SettingValue(SettingKey.ForcedMode, get(SettingKey.ForcedMode)),
        )
    }

    private fun isComponentEnabled(component: KClass<*>): Boolean {
        return context
            .packageManager
            .getComponentEnabledSetting(ComponentName(context, component.java)) ==
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
    }

    private fun setComponentEnabled(component: KClass<*>, enabled: Boolean) {
        context
            .packageManager
            .setComponentEnabledSetting(
                ComponentName(context, component.java),
                if (enabled) {
                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                } else {
                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED
                },
                PackageManager.DONT_KILL_APP,
            )
    }

    private suspend fun readBoolean(key: Preferences.Key<Boolean>, default: Boolean): Boolean {
        return context.dataStore.data.first()[key] ?: default
    }

    private suspend fun writeBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }
}
