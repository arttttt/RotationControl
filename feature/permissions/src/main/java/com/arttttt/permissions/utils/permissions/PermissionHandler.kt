package com.arttttt.permissions.utils.permissions

import androidx.activity.ComponentActivity
import com.arttttt.permissions.domain.entity.Permission2

interface PermissionHandler<T : Permission2> {
    suspend fun requestPermission(
        activity: ComponentActivity,
        permission: T,
    ): Permission2.Status
}