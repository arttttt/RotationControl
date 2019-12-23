package com.arttttt.rotationcontrolv3.utils.delegates.permissions.actions

import android.annotation.SuppressLint
import android.provider.Settings

@SuppressLint("InlinedApi")
sealed class Permissions(val action: String, val requestCode: Int) {
    class WriteSystemSettings: Permissions(Settings.ACTION_MANAGE_WRITE_SETTINGS, 0)
    class DrawOverlays: Permissions(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, 1)

    override fun equals(other: Any?): Boolean {
        if (other !is Permissions) return false

        return other.action == this.action && other.requestCode == this.requestCode
    }

    override fun hashCode(): Int {
        return action.hashCode() + requestCode.hashCode()
    }
}