package com.arttttt.rotationcontrolv3.utils.delegates.permissions.actions

import io.reactivex.Single

interface PermissionAction {
    val permission: Permissions
    fun checkPermission(): Single<Boolean>
}