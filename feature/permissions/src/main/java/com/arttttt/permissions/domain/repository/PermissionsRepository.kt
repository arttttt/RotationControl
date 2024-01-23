package com.arttttt.permissions.domain.repository

import com.arttttt.permissions.domain.entity.Permission2

interface PermissionsRepository {

    fun getRequiredPermissions(): List<Permission2>

    fun checkPermission(permission: Permission2): Permission2.Status
}