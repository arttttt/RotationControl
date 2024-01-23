package com.arttttt.permissions.data.repository

import android.content.Context
import com.arttttt.permissions.domain.entity.Permission2
import com.arttttt.permissions.domain.repository.PermissionsRepository

class PermissionsRepositoryImpl(
    private val context: Context
) : PermissionsRepository {

    private val permissions: List<Permission2> = listOf()

    override fun getRequiredPermissions(): List<Permission2> {
        return permissions
    }

    override fun checkPermission(permission: Permission2): Permission2.Status {
        return permission.checkStatus(context)
    }
}