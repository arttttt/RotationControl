package com.arttttt.permissions.presentation.handlers

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.domain.entity.StandardPermission
import com.arttttt.permissions.utils.extensions.of
import com.arttttt.permissions.utils.extensions.toBoolean
import com.arttttt.permissions.presentation.PermissionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class StandardPermissionHandler : PermissionHandler<StandardPermission> {

    private val resultFlow = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)

    override suspend fun requestPermission(
        activity: AppCompatActivity,
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
