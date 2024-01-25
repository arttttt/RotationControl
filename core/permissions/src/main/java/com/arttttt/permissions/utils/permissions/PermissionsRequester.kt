package com.arttttt.permissions.utils.permissions

import com.arttttt.permissions.domain.entity.Permission

interface PermissionsRequester {

    suspend fun requestPermission(permission: Permission): Permission.Status
}