package com.arttttt.rotationcontrolv3.domain.entity.apps

sealed interface AppEvent {

    val appInfo: AppInfo

    data class AppRemoved(
        override val appInfo: AppInfo
    ) : AppEvent

    data class AppInstalled(
        override val appInfo: AppInfo
    ) : AppEvent
}