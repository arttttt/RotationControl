package com.arttttt.rotationcontrolv3.utils.delegates.permissions

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.provider.Settings.System
import com.arttttt.rotationcontrolv3.R
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import com.arttttt.rotationcontrolv3.utils.extensions.intentOf

class AppPermissions(
    private val context: Context
): IPermissionsCheckerDelegate,
    IPermissionsRequesterDelegate {
    override fun canWriteSettings(): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) System.canWrite(context) else true

    override fun requestWriteSettingsPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
            System.canWrite(context)) {
            return
        }

        AlertDialog
            .Builder(context, R.style.AppTheme_Dialog_Alert)
            .setTitle(R.string.notice_text)
            .setMessage(R.string.can_write_settings_permission_text)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                context.startActivity(
                    intentOf(Settings.ACTION_MANAGE_WRITE_SETTINGS) {
                        data = Uri.parse("package:" + context.packageName)
                    }
                )
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show()
    }
}