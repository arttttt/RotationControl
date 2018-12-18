package com.arttttt.rotationcontrolv3.model.permissions

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.provider.Settings.System
import com.arttttt.rotationcontrolv3.R
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AlertDialog
import com.arttttt.rotationcontrolv3.model.permissions.base.PermissionsChecker
import com.arttttt.rotationcontrolv3.model.permissions.base.PermissionsRequester

class AppPermissions: PermissionsChecker, PermissionsRequester {
    override fun canWriteSettings(context: Context): Boolean = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) System.canWrite(context) else true

    override fun requestWriteSettingsPermission(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return
        if (System.canWrite(context))
            return

        AlertDialog.Builder(context, R.style.AppTheme_Dialog_Alert)
            .setTitle(R.string.notice_text)
            .setMessage(R.string.can_write_settings_permission_text)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
                    data = Uri.parse("package:" + context.packageName)
                }
                context.startActivity(intent)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show()
    }
}