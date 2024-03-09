package com.arttttt.rotationcontrolv3.domain.entity

sealed interface AppOrientation {

    data object Global : AppOrientation

    data object Portrait : AppOrientation

    data object PortraitReverse : AppOrientation

    data object Landscape : AppOrientation

    data object LandscapeReverse : AppOrientation

    data object Auto : AppOrientation
}