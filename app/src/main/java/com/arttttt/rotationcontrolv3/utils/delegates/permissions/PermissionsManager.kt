package com.arttttt.rotationcontrolv3.utils.delegates.permissions

import com.arttttt.rotationcontrolv3.utils.delegates.permissions.actions.Permissions
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.actions.PermissionAction
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.helper.StartForResult
import io.reactivex.Single

class PermissionsManager(
    private val startForResult: StartForResult,
    private val actions: Set<PermissionAction>
) {
    fun checkPermission(permission: Permissions): Single<Boolean> {
        return actions
            .first { action -> action.permission == permission }
            .checkPermission()
    }

    fun requestPermission(permission: Permissions): Single<Boolean> {
        return startForResult
            .startForResult(permission.action, permission.requestCode)
            .filter { requestCode -> requestCode == permission.requestCode }
            .flatMapSingle {
                actions
                    .first { action -> action.permission == permission }
                    .checkPermission()
            }
    }
}