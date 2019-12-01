package com.arttttt.rotationcontrolv3.utils.delegates.permissions.base

import android.app.Activity
import android.content.Intent
import io.reactivex.Observable
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

class PermissionResultHelper: IPermissionResultHelper, Consumer<Result> {

    private var appActivity: Activity? = null
    private val resultSubject = PublishSubject.create<Result>()

    override fun attachAppActivity(activity: Activity) {
        appActivity = activity
    }

    override fun detachAppActivity() {
        appActivity = null
    }

    override fun startForResult(intent: Intent, requestCode: Int) {
        appActivity?.startActivityForResult(intent, requestCode)
    }

    override fun resultObservable(): Observable<Result> {
        return resultSubject.share()
    }

    override fun accept(result: Result) {
        resultSubject.onNext(result)
    }
}