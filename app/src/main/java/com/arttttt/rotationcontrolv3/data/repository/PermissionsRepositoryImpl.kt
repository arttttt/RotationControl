package com.arttttt.rotationcontrolv3.data.repository

import android.content.Context
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import javax.inject.Inject

class PermissionsRepositoryImpl @Inject constructor(
    private val context: Context
) : PermissionsRepository {

    override fun checkPermission(permission: Permission): Permission.Status {
        return permission.checkStatus(context)
    }
}