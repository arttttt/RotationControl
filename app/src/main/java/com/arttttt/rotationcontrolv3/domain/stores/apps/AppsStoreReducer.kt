package com.arttttt.rotationcontrolv3.domain.stores.apps

import com.arkivanov.mvikotlin.core.store.Reducer

object AppsStoreReducer : Reducer<AppsStore.State, AppsStore.Message> {

    override fun AppsStore.State.reduce(msg: AppsStore.Message): AppsStore.State {
        return when (msg) {
            is AppsStore.Message.ProgressStarted -> copy(
                isInProgress = true,
            )

            is AppsStore.Message.ProgressFinished -> copy(
                isInProgress = false,
            )

            is AppsStore.Message.AppsLoaded -> copy(
                apps = msg.apps,
            )
        }
    }
}