package com.arttttt.rotationcontrolv3.utils.delegates.toast

import androidx.annotation.StringRes

interface IToastDelegate {
    fun showToast(@StringRes textRes: Int, duration: Int)
    fun showToast(message: String, duration: Int)
}