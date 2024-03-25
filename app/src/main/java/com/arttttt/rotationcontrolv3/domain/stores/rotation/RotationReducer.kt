package com.arttttt.rotationcontrolv3.domain.stores.rotation

import com.arkivanov.mvikotlin.core.store.Reducer

object RotationReducer : Reducer<RotationStore.State, RotationStore.Message> {

    override fun RotationStore.State.reduce(msg: RotationStore.Message): RotationStore.State {
        return when (msg) {
            is RotationStore.Message.GlobalOrientationReceived -> copy(
                globalOrientationMode = msg.orientationMode,
            )
            is RotationStore.Message.AppOrientationReceived -> copy(
                appOrientationMode = msg.orientationMode,
            )
            is RotationStore.Message.ErrorOccurred -> copy(
                globalOrientationMode = null,
                appOrientationMode = null,
                error = msg.error,
            )
        }
    }
}