package com.arttttt.rotationcontrolv3.domain.stores.apps

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.rotationcontrolv3.domain.entity.apps.AppInfo
import com.arttttt.rotationcontrolv3.domain.entity.apps.AppOrientation

interface AppsStore : Store<AppsStore.Intent, AppsStore.State, AppsStore.Label> {

    data class State(
        val isInProgress: Boolean,
        val apps: Map<String, AppInfo>,
    )

    sealed class Action {

        data object LoadApps : Action()
        data object SubscribeForAppsChanges : Action()
    }

    sealed class Intent {

        data object LoadApps : Intent()
        data class SetAppOrientation(
            val pkg: String,
            val appOrientation: AppOrientation,
        ) : Intent()
    }

    sealed class Message {

        data object ProgressStarted : Message()
        data object ProgressFinished : Message()
        data class AppsReceived(
            val apps: Map<String, AppInfo>,
        ) : Message()
    }

    sealed class Label
}