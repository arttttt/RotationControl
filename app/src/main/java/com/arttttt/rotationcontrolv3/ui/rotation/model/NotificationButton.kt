package com.arttttt.rotationcontrolv3.ui.rotation.model

sealed class NotificationButton {

    companion object;

    data object Auto : NotificationButton()
    data object Portrait : NotificationButton()
    data object PortraitReverse : NotificationButton()
    data object Landscape : NotificationButton()
    data object LandscapeReverse : NotificationButton()
}