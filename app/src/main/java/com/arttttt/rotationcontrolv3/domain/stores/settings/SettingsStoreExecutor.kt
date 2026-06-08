package com.arttttt.rotationcontrolv3.domain.stores.settings

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            is SettingsStore.Intent.UpdateSettingValue<*> -> updateSetting(intent)
        }
    }

    private fun loadSettings() {
        scope.launch {
            dispatch(
                SettingsStore.Message.SettingsLoaded(
                    settings = withContext(Dispatchers.IO) {
                        settingsRepository.getAll()
                    }
                )
            )
        }
    }

    private fun <T> updateSetting(intent: SettingsStore.Intent.UpdateSettingValue<T>) {
        scope.launch {
            withContext(Dispatchers.IO) {
                settingsRepository.save(intent.key, intent.value)
            }

            dispatch(
                SettingsStore.Message.SettingsLoaded(
                    settings = withContext(Dispatchers.IO) {
                        settingsRepository.getAll()
                    }
                )
            )
        }
    }
}
