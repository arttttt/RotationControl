package com.arttttt.permissions.domain.repository

import com.arttttt.permissions.domain.entity.Permission

interface PermissionsRepository {

    fun getRequiredPermissions(): List<Permission>

    fun checkPermission(permission: Permission): Permission.Status
}