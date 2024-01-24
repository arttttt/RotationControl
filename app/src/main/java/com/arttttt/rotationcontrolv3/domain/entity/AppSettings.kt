package com.arttttt.rotationcontrolv3.domain.entity

sealed class AppSettings {

    data class StartOnBoot(
        val settings: Settings,
        val value: Boolean,
    ) : AppSettings()
}