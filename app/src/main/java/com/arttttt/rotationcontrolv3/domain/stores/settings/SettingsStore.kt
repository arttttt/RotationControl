package com.arttttt.rotationcontrolv3.domain.stores.settings

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.rotationcontrolv3.domain.entity.settings.SettingKey
import com.arttttt.rotationcontrolv3.domain.entity.settings.SettingValue

interface SettingsStore : Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label> {

    data class State(
        val settings: List<SettingValue<*>>,
    )

    sealed class Action {

        data object LoadSettings : Action()
    }

    sealed class Intent {

        data class UpdateSettingValue<T>(
            val key: SettingKey<T>,
            val value: T,
        ) : Intent()
    }

    sealed class Message {

        data class SettingsLoaded(
            val settings: List<SettingValue<*>>
        ) : Message()
    }

    sealed class Label
}
