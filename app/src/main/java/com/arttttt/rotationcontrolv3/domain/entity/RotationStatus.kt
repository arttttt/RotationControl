package com.arttttt.rotationcontrolv3.domain.entity

sealed class RotationStatus {

    data object Enabled : RotationStatus()

    data object Disabled : RotationStatus()
}