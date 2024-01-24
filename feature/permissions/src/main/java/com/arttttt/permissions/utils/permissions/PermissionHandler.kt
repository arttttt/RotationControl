package com.arttttt.permissions.utils.permissions

import androidx.activity.ComponentActivity
import com.arttttt.permissions.domain.entity.Permission

interface PermissionHandler<T : Permission> {
    suspend fun requestPermission(
        activity: ComponentActivity,
        permission: T,
    ): Permission.Status
}