package com.arttttt.rotationcontrolv3.utils.delegates.toast

import android.content.Context
import com.arttttt.rotationcontrolv3.utils.extensions.android.toastOf

class ToastDelegate(
    private val context: Context
): IToastDelegate {
    override fun showToast(textRes: Int, duration: Int) {
        toastOf(context, textRes, duration)
    }

    override fun showToast(message: String, duration: Int) {
        toastOf(context, message, duration)
    }
}