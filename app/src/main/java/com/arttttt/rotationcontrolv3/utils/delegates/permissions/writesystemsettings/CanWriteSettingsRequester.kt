package com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings

import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.Settings
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.base.IResultHelper
import com.arttttt.rotationcontrolv3.utils.extensions.android.intentOf
import io.reactivex.Single

class CanWriteSettingsRequester(
    private val context: Context,
    private val helper: IResultHelper,
    private val canWriteSettingsChecker: ICanWriteSettingsChecker
): ICanWriteSettingsRequester {

    companion object {
        private const val REQUEST_CODE = 1
    }

    override fun requestWriteSettingsPermission(): Single<Boolean> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            helper.startForResult(
                intentOf(Settings.ACTION_MANAGE_WRITE_SETTINGS) {
                    data = Uri.parse("package:" + context.packageName)
                },
                REQUEST_CODE
            )

            return helper
                .onResultReceived()
                .filter { result -> result.requestCode == REQUEST_CODE }
                .firstOrError()
                .flatMap { canWriteSettingsChecker.canWriteSettings() }
        } else {
            return Single.just(true)
        }
    }
}