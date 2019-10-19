package com.arttttt.rotationcontrolv3.utils.delegates.permissions.base

import android.content.Intent

data class Result(
    val requestCode: Int,
    val resultCode: Int,
    val data: Intent?
)