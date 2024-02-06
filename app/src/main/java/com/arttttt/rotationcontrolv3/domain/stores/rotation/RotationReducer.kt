package com.arttttt.rotationcontrolv3.domain.stores.rotation

import com.arkivanov.mvikotlin.core.store.Reducer

object RotationReducer : Reducer<RotationStore.State, RotationStore.Message> {

    override fun RotationStore.State.reduce(msg: RotationStore.Message): RotationStore.State {
        return when (msg) {
            is RotationStore.Message.OrientationReceived -> copy(
                orientationMode = msg.orientationMode,
            )
            is RotationStore.Message.ErrorOccurred -> copy(
                orientationMode = null,
                error = msg.error,
            )
        }
    }
}