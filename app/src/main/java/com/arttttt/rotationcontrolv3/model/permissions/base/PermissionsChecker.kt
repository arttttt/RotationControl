package com.arttttt.rotationcontrolv3.model.permissions.base

import android.content.Context

interface PermissionsChecker {
    fun canWriteSettings(context: Context): Boolean
}