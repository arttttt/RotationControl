package com.arttttt.rotationcontrolv3.domain.stores.settings

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.rotationcontrolv3.domain.entity.settings.Setting
import kotlin.reflect.KClass

interface SettingsStore : Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label> {

    data class State(
        val settings: List<Setting<*>>,
    )

    sealed class Action {

        data object LoadSettings : Action()
    }

    sealed class Intent {

        data class UpdateSettingValue<T>(
            val value: T,
            val clazz: KClass<out Setting<T>>,
        ) : Intent()
    }

    sealed class Message {

        data class SettingsLoaded(
            val settings: List<Setting<*>>
        ) : Message()
    }

    sealed class Label
}