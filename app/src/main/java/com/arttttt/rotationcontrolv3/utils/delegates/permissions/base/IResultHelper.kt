package com.arttttt.rotationcontrolv3.utils.delegates.permissions.base

import android.content.Intent
import io.reactivex.Observable

interface IResultHelper {
    fun startForResult(intent: Intent, requestCode: Int)
    fun onResultReceived(): Observable<Result>
}