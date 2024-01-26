package com.arttttt.rotationcontrolv3.data.model

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.domain.entity.StandardPermission
import com.arttttt.permissions.utils.extensions.checkStatusImpl

data object NotificationsPermission : StandardPermission by if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) StandardNoOpPermission else Impl33 {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private data object Impl33 : StandardPermission {

        override val permission: String = Manifest.permission.POST_NOTIFICATIONS

        override fun checkStatus(context: Context): Permission.Status {
            return checkStatusImpl(context)
        }
    }
}