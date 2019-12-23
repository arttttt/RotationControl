package com.arttttt.rotationcontrolv3.utils.delegates.permissions.actions

import android.content.Context
import android.os.Build
import android.provider.Settings
import io.reactivex.Single

class DrawOverlayAction(
    private val context: Context
): PermissionAction {

    override val permission: Permissions = Permissions.DrawOverlays()

    override fun checkPermission(): Single<Boolean> {
        return Single.fromCallable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                Settings.canDrawOverlays(context)
            else
                true
        }
    }
}