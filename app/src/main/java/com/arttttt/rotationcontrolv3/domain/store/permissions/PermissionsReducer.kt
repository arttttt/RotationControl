package com.arttttt.rotationcontrolv3.domain.store.permissions

import com.arkivanov.mvikotlin.core.store.Reducer

object PermissionsReducer : Reducer<PermissionsStore.State, PermissionsStore.Message> {

    override fun PermissionsStore.State.reduce(msg: PermissionsStore.Message): PermissionsStore.State {
        return when (msg) {
            is PermissionsStore.Message.PermissionsReceived -> copy(
                permissions = msg.permissions
            )
            is PermissionsStore.Message.ProgressStarted -> copy(
                isInProgress = true
            )
            is PermissionsStore.Message.ProgressFinished -> copy(
                isInProgress = false,
            )
        }
    }
}