package com.arttttt.rotationcontrolv3.domain.stores.apps

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import javax.inject.Inject

class AppsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val appsRepository: AppsRepository,
) {

    fun create(): AppsStore {
        val name = AppsStore::class.qualifiedName
        val initialState = AppsStore.State(
            isInProgress = false,
            apps = emptyList(),
        )

        return object : AppsStore,
            Store<AppsStore.Intent, AppsStore.State, AppsStore.Label> by storeFactory.create(
                name = name,
                initialState = initialState,
                bootstrapper = SimpleBootstrapper(
                    AppsStore.Action.LoadApps,
                ),
                executorFactory = {
                    AppsStoreExecutor(
                        appsRepository = appsRepository,
                    )
                },
                reducer = AppsStoreReducer,
            ) {}
    }
}