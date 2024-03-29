package com.arttttt.rotationcontrolv3.framework.model

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import com.arttttt.permissions.data.model.IntentNoOpPermission
import com.arttttt.permissions.domain.entity.IntentPermission
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.utils.extensions.of

data object DrawOverlayPermission : IntentPermission by if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) IntentNoOpPermission else Impl23 {

    @RequiresApi(Build.VERSION_CODES.M)
    private data object Impl23 : IntentPermission {

        override fun createIntent(context: Context): Intent {
            return Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION).apply {
                setData("package:${context.packageName}".toUri())
            }
        }

        override fun checkStatus(context: Context): Permission.Status {
            return Permission.Status.of(Settings.canDrawOverlays(context))
        }
    }
}