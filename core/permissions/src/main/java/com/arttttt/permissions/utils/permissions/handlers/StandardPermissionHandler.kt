package com.arttttt.permissions.utils.permissions.handlers

import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.arttttt.permissions.utils.permissions.PermissionHandler
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.domain.entity.StandardPermission
import com.arttttt.permissions.utils.extensions.of
import com.arttttt.permissions.utils.extensions.toBoolean
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class StandardPermissionHandler : PermissionHandler<StandardPermission> {

    private val resultFlow = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)

    override suspend fun requestPermission(
        activity: ComponentActivity,
        permission: StandardPermission
    ): Permission.Status {
        if (permission.checkStatus(activity.applicationContext).toBoolean()) {
            return Permission.Status.Granted
        }

        val key = "permission_request_${permission.permission}"

        val callback = ActivityResultCallback<Boolean> { result ->
            resultFlow.tryEmit(result)
        }

        val launcher = activity.activityResultRegistry.register(key, ActivityResultContracts.RequestPermission(), callback)

        return try {
            launcher.launch(permission.permission)
            Permission.Status.of(resultFlow.first())
        } finally {
            launcher.unregister()
        }
    }
}
