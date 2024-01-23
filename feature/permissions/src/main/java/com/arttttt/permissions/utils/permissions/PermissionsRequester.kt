package com.arttttt.permissions.utils.permissions

import com.arttttt.permissions.domain.entity.Permission2

interface PermissionsRequester {

    suspend fun requestPermission(permission: Permission2): Permission2.Status
}