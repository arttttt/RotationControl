package com.arttttt.permissions.presentation.handlers

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.arttttt.permissions.domain.entity.IntentPermission
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.utils.extensions.of
import com.arttttt.permissions.utils.extensions.toBoolean
import com.arttttt.permissions.presentation.PermissionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class StartForResultPermissionHandler<T : IntentPermission> : PermissionHandler<T> {

    private val resultFlow = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)

    override suspend fun requestPermission(
        activity: AppCompatActivity,
        permission: T
    ): Permission.Status {
        if (permission.checkStatus(activity.applicationContext).toBoolean()) {
            return Permission.Status.Granted
        }

        val key = "permission_request_${permission}"

        val callback = ActivityResultCallback<Boolean> { result ->
            resultFlow.tryEmit(result)
        }

        val launcher = activity.activityResultRegistry.register(
            key,
            object : ActivityResultContract<Unit, Boolean>() {
                override fun createIntent(context: Context, input: Unit): Intent {
                    return permission.createIntent(activity)
                }

                override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
                    return permission.checkStatus(activity.applicationContext) == Permission.Status.Granted
                }
            },
            callback,
        )

        return try {
            launcher.launch(Unit)
            Permission.Status.of(resultFlow.first())
        } finally {
            launcher.unregister()
        }
    }
}