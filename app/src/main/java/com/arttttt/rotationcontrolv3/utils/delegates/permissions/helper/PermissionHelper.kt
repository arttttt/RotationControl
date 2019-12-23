package com.arttttt.rotationcontrolv3.utils.delegates.permissions.helper

import android.net.Uri
import com.arttttt.rotationcontrolv3.presentation.AppActivity
import com.arttttt.rotationcontrolv3.utils.extensions.android.intentOf
import io.reactivex.Single
import io.reactivex.functions.Consumer
import io.reactivex.subjects.PublishSubject

class PermissionHelper: ActivityHolder, StartForResult, Consumer<Int> {

    private var activity: AppActivity? = null

    private val subject = PublishSubject.create<Int>()

    override fun attachActivity(activity: AppActivity) {
        this.activity = activity
    }

    override fun detachActivity() {
        activity = null
    }

    override fun startForResult(action: String, requestCode: Int): Single<Int> {
        activity?.let { activity ->
            activity.startActivityForResult(
                intentOf(action) {
                    data = Uri.parse("package:" + activity.packageName)
                },
                requestCode
            )
        }

        return subject.firstOrError()
    }

    override fun accept(result: Int) {
        subject.onNext(result)
    }
}