package com.arttttt.rotationcontrolv3.utils

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.provider.Settings
import com.arttttt.rotationcontrolv3.utils.extensions.android.getInt
import com.arttttt.rotationcontrolv3.utils.extensions.android.registerContentObserver
import io.reactivex.Observable

class AccelerometerObserver(
    private val contentResolver: ContentResolver
) {
    private val observable = Observable.create<Int> { emitter ->
        val observer = object: ContentObserver(null) {
            override fun onChange(selfChange: Boolean, uri: Uri) {
                emitter.onNext(contentResolver.getInt(uri.schemeSpecificPart.substringAfterLast('/')))
            }
        }

        emitter.setCancellable {
            contentResolver.unregisterContentObserver(observer)
        }

        contentResolver.registerContentObserver(
            Settings.System.ACCELEROMETER_ROTATION,
            false,
            observer
        )
    }

    fun accelerometerChanges(): Observable<Int> = observable.share()
}
