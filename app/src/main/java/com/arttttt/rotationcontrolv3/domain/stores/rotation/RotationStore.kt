package com.arttttt.rotationcontrolv3.domain.stores.rotation

import com.arkivanov.mvikotlin.core.store.Store
import com.arttttt.rotationcontrolv3.domain.entity.rotation.OrientationMode

interface RotationStore : Store<RotationStore.Intent, RotationStore.State, RotationStore.Label> {

    data class State(
        val globalOrientationMode: OrientationMode?,
        val appOrientationMode: OrientationMode?,
        val error: Throwable?,
    )

    sealed class Action {

        data object GetGlobalOrientation : Action()
    }

    sealed class Intent {

        data class SetGlobalOrientationMode(
            val orientationMode: OrientationMode,
        ) : Intent()

        data class SetAppOrientationMode(
            val orientationMode: OrientationMode?,
        ) : Intent()
    }

    sealed class Message {

        data class GlobalOrientationReceived(
            val orientationMode: OrientationMode,
        ) : Message()

        data class AppOrientationReceived(
            val orientationMode: OrientationMode?,
        ) : Message()

        data class ErrorOccurred(
            val error: Throwable
        ) : Message()
    }

    sealed class Label
}