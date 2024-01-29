package com.arttttt.rotationcontrolv3.domain.entity

sealed class OrientationMode {

    abstract val value: Int

    data object Portrait : OrientationMode() {

        override val value: Int = 0
    }

    data object Landscape : OrientationMode() {

        override val value: Int = 1
    }

    data object PortraitReverse : OrientationMode() {

        override val value: Int = 2
    }

    data object LandscapeReverse : OrientationMode() {

        override val value: Int = 3
    }

    data object Auto : OrientationMode() {

        override val value: Int = 4
    }
}