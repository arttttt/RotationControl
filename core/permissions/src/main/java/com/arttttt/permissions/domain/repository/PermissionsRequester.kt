package com.arttttt.permissions.domain.repository

import com.arttttt.permissions.domain.entity.Permission

interface PermissionsRequester {

    suspend fun requestPermission(permission: Permission): Permission.Status
}