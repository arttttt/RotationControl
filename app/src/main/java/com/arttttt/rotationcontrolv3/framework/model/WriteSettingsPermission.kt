package com.arttttt.rotationcontrolv3.framework.model

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.arttttt.permissions.data.model.IntentNoOpPermission
import com.arttttt.permissions.domain.entity.IntentPermission
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.utils.extensions.of

data object WriteSettingsPermission : IntentPermission by if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) IntentNoOpPermission else Impl23 {

    @RequiresApi(Build.VERSION_CODES.M)
    private data object Impl23 : IntentPermission {

        override fun createIntent(context: Context): Intent {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            intent.setData(Uri.parse("package:" + context.packageName))

            return intent
        }

        override fun checkStatus(context: Context): Permission.Status {
            return Permission.Status.of(Settings.System.canWrite(context))
        }
    }
}