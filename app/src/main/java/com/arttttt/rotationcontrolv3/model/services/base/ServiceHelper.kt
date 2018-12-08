package com.arttttt.rotationcontrolv3.model.services.base

import android.arch.lifecycle.LiveData
import android.content.Context

interface ServiceHelper {
    fun isStarted(): LiveData<Boolean>
    fun startService(context: Context)
    fun stopService(context: Context)
}