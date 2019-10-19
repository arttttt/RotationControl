@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.arttttt.rotationcontrolv3.device.services.delegate

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.PixelFormat
import android.view.Surface
import android.view.View
import android.view.WindowManager
import com.arttttt.rotationcontrolv3.device.services.rotation.RotationService
import com.arttttt.rotationcontrolv3.utils.extensions.koilin.unsafeCastTo

class WindowDelegate(
    private val context: Context
): IWindowDelegate {
    private val rootView: View by lazy { View(context) }
    private val windowService: WindowManager by lazy { context.getSystemService(Context.WINDOW_SERVICE).unsafeCastTo<WindowManager>() }

    private var isWindowInitialized = false

    override fun createOrUpdateWindow(orientation: RotationService.Orientation) {
        val params = WindowManager.LayoutParams(
            1, 1,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            this.screenOrientation = rotationServiceOrientationToActivityOrientation(orientation)
        }

        if (isWindowInitialized) {
            windowService.updateViewLayout(rootView, params)
        } else {
            windowService.addView(rootView, params)
            isWindowInitialized = true
        }
    }

    override fun removeWindow() {
        if (!isWindowInitialized) return

        windowService.removeViewImmediate(rootView)
    }

    private fun rotationServiceOrientationToActivityOrientation(orientation: RotationService.Orientation): Int {
        return when(orientation) {
            RotationService.Orientation.ORIENTATION_PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            RotationService.Orientation.ORIENTATION_LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            RotationService.Orientation.ORIENTATION_PORTRAIT_REVERSE -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
            RotationService.Orientation.ORIENTATION_LANDSCAPE_REVERSE -> ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
            RotationService.Orientation.ORIENTATION_AUTO -> ActivityInfo.SCREEN_ORIENTATION_SENSOR
        }
    }
}