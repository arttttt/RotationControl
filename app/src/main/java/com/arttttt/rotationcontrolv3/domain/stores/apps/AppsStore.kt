package com.arttttt.rotationcontrolv3.domain.stores.apps

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.rotationcontrolv3.domain.entity.AppInfo

interface AppsStore : Store<AppsStore.Intent, AppsStore.State, AppsStore.Label> {

    data class State(
        val isInProgress: Boolean,
        val apps: List<AppInfo>,
    )

    sealed class Action {

        data object LoadApps : Action()
    }

    sealed class Intent

    sealed class Message {

        data object ProgressStarted : Message()
        data object ProgressFinished : Message()
        data class AppsLoaded(
            val apps: List<AppInfo>,
        ) : Message()
    }

    sealed class Label
}