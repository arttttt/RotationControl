package com.arttttt.rotationcontrolv3.domain.stores

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository

class SettingsStoreFactory(
    private val storeFactory: StoreFactory,
    private val settingsRepository: SettingsRepository,
) {

    fun create(): SettingsStore {
        return object : SettingsStore,
            Store<SettingsStore.Intent, SettingsStore.State, SettingsStore.Label> by storeFactory.create(
                name = SettingsStore::class.java.name,
                initialState = SettingsStore.State(
                    settings = emptyList(),
                ),
                bootstrapper = SimpleBootstrapper(SettingsStore.Action.LoadSettings),
                executorFactory = {
                    SettingsStoreExecutor(
                        settingsRepository = settingsRepository,
                    )
                },
                reducer = SettingsReducer,
            ) {}
    }
}