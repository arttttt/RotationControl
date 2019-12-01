package com.arttttt.rotationcontrolv3.utils.delegates.permissions.base

import android.app.Activity
import android.content.Intent
import io.reactivex.Observable

interface IPermissionResultHelper {
    fun attachAppActivity(activity: Activity)
    fun detachAppActivity()
    fun startForResult(intent: Intent, requestCode: Int)
    fun resultObservable(): Observable<Result>
}