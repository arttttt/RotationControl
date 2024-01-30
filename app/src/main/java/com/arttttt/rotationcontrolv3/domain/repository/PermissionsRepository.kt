package com.arttttt.rotationcontrolv3.domain.repository

import com.arttttt.permissions.domain.entity.Permission

interface PermissionsRepository {

    fun checkPermission(permission: Permission): Permission.Status
}