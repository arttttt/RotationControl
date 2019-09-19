package com.arttttt.rotationcontrolv3.device.services.base

import androidx.lifecycle.LiveData
import android.content.Context

interface ServiceHelper {
    fun isStarted(): LiveData<Boolean>
    fun startService(context: Context)
    fun stopService(context: Context)
}