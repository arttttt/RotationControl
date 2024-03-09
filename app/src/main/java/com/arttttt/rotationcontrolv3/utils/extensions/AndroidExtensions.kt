package com.arttttt.rotationcontrolv3.utils.extensions

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.provider.Settings

inline fun <reified T : AccessibilityService> Context.isAccessibilityServiceEnabled(): Boolean {
    val accessibilityCode = Settings.Secure.getInt(
        contentResolver,
        Settings.Secure.ACCESSIBILITY_ENABLED,
    )

    return when (accessibilityCode) {
        1 -> {
            Settings.Secure
                .getString(
                    contentResolver,
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES,
                )
                .contains("$packageName/${T::class.qualifiedName}")
        }

        else -> false
    }
}