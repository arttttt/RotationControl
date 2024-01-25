package com.arttttt.permissions.utils.permissions

import androidx.appcompat.app.AppCompatActivity
import com.arttttt.permissions.domain.entity.Permission

interface PermissionHandler<T : Permission> {
    suspend fun requestPermission(
        activity: AppCompatActivity,
        permission: T,
    ): Permission.Status
}