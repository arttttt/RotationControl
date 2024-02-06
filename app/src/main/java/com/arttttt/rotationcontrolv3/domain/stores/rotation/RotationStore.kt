package com.arttttt.rotationcontrolv3.domain.stores.rotation

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.rotationcontrolv3.domain.entity.OrientationMode

interface RotationStore : Store<RotationStore.Intent, RotationStore.State, RotationStore.Label> {

    data class State(
        val orientationMode: OrientationMode?,
        val error: Throwable?,
    )

    sealed class Action {

        data object GetOrientation : Action()
    }

    sealed class Intent {

        data class SetOrientationMode(
            val orientationMode: OrientationMode,
        ) : Intent()
    }

    sealed class Message {

        data class OrientationReceived(
            val orientationMode: OrientationMode
        ) : Message()

        data class ErrorOccurred(
            val error: Throwable
        ) : Message()
    }

    sealed class Label
}