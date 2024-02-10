package com.arttttt.permissions.presentation.handlers

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.arttttt.permissions.domain.entity.IntentPermission
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.domain.entity.StandardPermission
import com.arttttt.permissions.presentation.PermissionHandler
import com.arttttt.permissions.utils.extensions.checkStatusImpl
import com.arttttt.permissions.utils.extensions.of
import com.arttttt.permissions.utils.extensions.toBoolean
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first

class StandardPermissionHandler : PermissionHandler<StandardPermission> {

    private val startForResultPermissionHandler by lazy {
        StartForResultPermissionHandler<IntentPermission>()
    }

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

            when (val status = Permission.Status.of(resultFlow.first())) {
                Permission.Status.Denied -> handleDeny(
                    originalStatus = status,
                    activity = activity,
                    permission = permission,
                )
                Permission.Status.Granted -> status
            }
        } finally {
            launcher.unregister()
        }
    }

    private suspend fun handleDeny(
        originalStatus: Permission.Status,
        activity: AppCompatActivity,
        permission: StandardPermission,
    ): Permission.Status {
        return if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission.permission)) {
            originalStatus
        } else {
            startForResultPermissionHandler.requestPermission(
                activity = activity,
                permission = createIntentPermission(
                    packageName = activity.packageName,
                    permission = permission,
                )
            )
        }
    }

    private fun createIntentPermission(
        packageName: String,
        permission: StandardPermission,
    ): IntentPermission {
        return object : IntentPermission {
            override fun createIntent(context: Context): Intent {
                return Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
            }

            override fun checkStatus(context: Context): Permission.Status {
                return checkStatusImpl(
                    context = context,
                    permission = permission.permission,
                )
            }
        }
    }
}
