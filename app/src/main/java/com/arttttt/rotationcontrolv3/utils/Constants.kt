package com.arttttt.rotationcontrolv3.utils

import android.os.Build
import android.view.WindowManager

const val PREFERENCES_NAME = "settings"

const val SAVED_ORIENTATION ="saved_orientation"
const val START_ON_BOOT = "start_on_boot"
const val FORCE_MODE = "force_mode"

const val APP_CICERONE = "APP_CICERONE"
const val APP_HOLDER = "APP_HOLDER"
const val APP_ROUTER = "APP_ROUTER"

const val FLOW_CICERONE = "FLOW_CICERONE"
const val FLOW_HOLDER = "FLOW_HOLDER"
const val FLOW_ROUTER = "FLOW_ROUTER"

val WINDOW_TYPE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
} else {
    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
}