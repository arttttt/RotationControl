package com.arttttt.rotationcontrolv3.utils.delegates.permissions.drawoverlays

import android.content.Context
import android.os.Build
import android.provider.Settings
import io.reactivex.Single

class CanDrawOverlayChecker(
    private val context: Context
): ICanDrawOverlayChecker {
    override fun canDrawOverlay(): Single<Boolean> {
        return Single.fromCallable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                Settings.canDrawOverlays(context)
            else
                true
        }
    }
}