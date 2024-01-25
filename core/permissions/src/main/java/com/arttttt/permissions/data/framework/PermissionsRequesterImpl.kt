package com.arttttt.permissions.data.framework

import androidx.appcompat.app.AppCompatActivity
import com.arttttt.permissions.domain.repository.PermissionsRequester
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.domain.entity.StandardPermission
import com.arttttt.permissions.presentation.PermissionHandler
import com.arttttt.utils.castTo
import kotlin.reflect.KClass

class PermissionsRequesterImpl(
    private val activity: AppCompatActivity,
    private val handlers: Map<KClass<out Permission>, PermissionHandler<*>>,
) : PermissionsRequester {

    @Suppress("ReplaceGetOrSet")
    override suspend fun requestPermission(permission: Permission): Permission.Status {
        return when (permission) {
            is StandardPermission -> handlers.get(StandardPermission::class)
            else -> handlers.get(permission::class)
        }
            ?.castTo<PermissionHandler<Permission>>()
            ?.requestPermission(activity, permission)
            ?: throw IllegalArgumentException("Permission handler not fount: $permission")
    }
}