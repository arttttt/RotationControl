@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

package com.arttttt.rotationcontrolv3.device.services.delegate

import android.content.pm.ActivityInfo
import android.graphics.PixelFormat
import android.view.View
import android.view.WindowManager
import com.arttttt.rotationcontrolv3.device.services.rotation.RotationService
import com.arttttt.rotationcontrolv3.utils.IViewProvider
import com.arttttt.rotationcontrolv3.utils.WINDOW_TYPE

class WindowDelegate(
    viewProvider: IViewProvider,
    private val windowManager: WindowManager
): IWindowDelegate {

    companion object {
        private const val WINDOW_SIDE_SIZE = 1
    }

    private val windowLayoutParameters = WindowManager.LayoutParams(
        WINDOW_SIDE_SIZE,
        WINDOW_SIDE_SIZE,
        WINDOW_TYPE,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )
    private val rootView: View = viewProvider.provideView()
    private var isWindowInitialized = false

    override fun createOrUpdateWindow(orientation: RotationService.Orientation) {
        windowLayoutParameters.screenOrientation = rotationServiceOrientationToActivityOrientation(orientation)

        if (isWindowInitialized) {
            windowManager.updateViewLayout(rootView, windowLayoutParameters)
        } else {
            windowManager.addView(rootView, windowLayoutParameters)
            isWindowInitialized = true
        }
    }

    override fun removeWindow() {
        if (!isWindowInitialized) return

        windowManager.removeViewImmediate(rootView)
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