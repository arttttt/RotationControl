package com.arttttt.rotationcontrolv3.domain.entity

sealed class Setting<T> {

    abstract val value: T

    data class StartOnBoot(
        override val value: Boolean,
    ) : Setting<Boolean>()

    data class ForcedMode(
        override val value: Boolean,
    ) : Setting<Boolean>()
}