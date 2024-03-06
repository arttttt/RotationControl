package com.arttttt.rotationcontrolv3.domain.stores.settings

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.rotationcontrolv3.domain.entity.settings.Setting
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.utils.unsafeCastTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.reflect.KClass

class SettingsStoreExecutor(
    private val settingsRepository: SettingsRepository,
) : CoroutineExecutor<SettingsStore.Intent, SettingsStore.Action, SettingsStore.State, SettingsStore.Message, SettingsStore.Label>() {

    override fun executeAction(action: SettingsStore.Action) {
        when (action) {
            is SettingsStore.Action.LoadSettings -> loadSettings()
        }
    }

    override fun executeIntent(intent: SettingsStore.Intent) {
        when (intent) {
            is SettingsStore.Intent.UpdateSettingValue<*> -> {
                updateSetting(
                    clazz = intent.clazz.unsafeCastTo(),
                    value = intent.value,
                )
            }
        }
    }

    private fun loadSettings() {
        scope.launch {
            val settings = withContext(Dispatchers.IO) {
                settingsRepository.getAllSettings()
            }

            dispatch(
                SettingsStore.Message.SettingsLoaded(
                    settings = settings,
                )
            )
        }
    }

    private fun <T> updateSetting(clazz: KClass<out Setting<T>>, value: T) {
        scope.launch {
            withContext(Dispatchers.IO) {
                settingsRepository.saveSetting(clazz, value)
            }

            dispatch(
                SettingsStore.Message.SettingsLoaded(
                    settings = withContext(Dispatchers.IO) {
                        settingsRepository.getAllSettings()
                    }
                )
            )
        }
    }
}