package com.arttttt.rotationcontrolv3.domain.entity.rotation

sealed class RotationStatus {

    data object Enabled : RotationStatus()

    data object Disabled : RotationStatus()
}