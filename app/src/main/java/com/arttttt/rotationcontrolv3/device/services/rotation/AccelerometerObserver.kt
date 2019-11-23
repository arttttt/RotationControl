package com.arttttt.rotationcontrolv3.device.services.rotation

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.provider.Settings
import com.arttttt.rotationcontrolv3.utils.extensions.android.getInt
import com.arttttt.rotationcontrolv3.utils.extensions.android.registerContentObserver
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import timber.log.Timber

class AccelerometerObserver private constructor(
    private val contentResolver: ContentResolver
): ObservableOnSubscribe<Int> {
    companion object {
        fun accelerometerChanges(contentResolver: ContentResolver): Observable<Int> {
            return Observable.create(AccelerometerObserver(contentResolver)).share()
        }
    }

    override fun subscribe(emitter: ObservableEmitter<Int>) {
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
}