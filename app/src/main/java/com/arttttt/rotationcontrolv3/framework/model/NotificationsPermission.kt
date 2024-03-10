package com.arttttt.rotationcontrolv3.framework.model

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import com.arttttt.permissions.data.model.StandardNoOpPermission
import com.arttttt.permissions.domain.entity.StandardPermission

data object NotificationsPermission : StandardPermission by if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) StandardNoOpPermission else Impl33 {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private data object Impl33 : StandardPermission {

        override val permission: String = Manifest.permission.POST_NOTIFICATIONS
    }
}