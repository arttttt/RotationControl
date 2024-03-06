package com.arttttt.rotationcontrolv3.domain.managers

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.core.content.getSystemService
import com.arttttt.rotationcontrolv3.domain.entity.rotation.OrientationMode
import java.util.concurrent.atomic.AtomicBoolean

class ForcedOrientationManager(
    context: Context,
) {

    companion object {
        private const val WINDOW_SIDE_SIZE = 1

        @Suppress("DEPRECATION")
        private val WINDOW_TYPE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        }
    }

    private val windowLayoutParameters = WindowManager.LayoutParams(
        WINDOW_SIDE_SIZE,
        WINDOW_SIDE_SIZE,
        WINDOW_TYPE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_SECURE,
        PixelFormat.TRANSLUCENT
    )

    private val rootView: View = View(context)
    private var isWindowInitialized = AtomicBoolean(false)

    private val windowManager = context.getSystemService<WindowManager>()!!

    fun forceApplyOrientation(mode: OrientationMode) {
        windowLayoutParameters.screenOrientation = mode.toActivityOrientation()

        if (isWindowInitialized.get()) {
            windowManager.updateViewLayout(rootView, windowLayoutParameters)
        } else {
            windowManager.addView(rootView, windowLayoutParameters)
            isWindowInitialized.set(true)
        }
    }

    fun clear() {
        if (!isWindowInitialized.get()) return

        windowManager.removeViewImmediate(rootView)
    }

    private fun OrientationMode.toActivityOrientation(): Int {
        return when (this) {
            OrientationMode.Portrait -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            OrientationMode.Landscape -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            OrientationMode.PortraitReverse -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            OrientationMode.LandscapeReverse -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            OrientationMode.Auto -> ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
    }
}