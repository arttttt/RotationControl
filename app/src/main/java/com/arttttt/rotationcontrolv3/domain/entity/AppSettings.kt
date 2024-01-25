package com.arttttt.rotationcontrolv3.domain.entity

sealed class AppSettings {

    data class StartOnBoot(
        val value: Boolean,
    ) : AppSettings()
}