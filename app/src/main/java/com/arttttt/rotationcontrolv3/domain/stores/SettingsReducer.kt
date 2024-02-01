package com.arttttt.rotationcontrolv3.domain.stores

import com.arkivanov.mvikotlin.core.store.Reducer

data object SettingsReducer : Reducer<SettingsStore.State, SettingsStore.Message> {

    override fun SettingsStore.State.reduce(msg: SettingsStore.Message): SettingsStore.State {
        return when (msg) {
            is SettingsStore.Message.SettingsLoaded -> copy(
                settings = msg.settings,
            )
        }
    }
}