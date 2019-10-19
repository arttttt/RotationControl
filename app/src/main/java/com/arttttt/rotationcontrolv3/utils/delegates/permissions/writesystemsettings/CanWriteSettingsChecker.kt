package com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings

import android.content.Context
import android.os.Build
import android.provider.Settings
import io.reactivex.Single

class CanWriteSettingsChecker(
    private val context: Context
): ICanWriteSettingsChecker {
    override fun canWriteSettings(): Single<Boolean> {
        return Single.fromCallable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                Settings.System.canWrite(context)
            else
                true
        }
    }
}