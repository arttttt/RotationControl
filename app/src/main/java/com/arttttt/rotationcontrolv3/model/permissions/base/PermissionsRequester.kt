package com.arttttt.rotationcontrolv3.model.permissions.base

import android.content.Context

interface PermissionsRequester {
    fun requestWriteSettingsPermission(context: Context)
}