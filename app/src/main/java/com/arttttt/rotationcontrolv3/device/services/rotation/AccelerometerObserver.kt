package com.arttttt.rotationcontrolv3.device.services.rotation

import android.database.ContentObserver
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

class AccelerometerObserver: ContentObserver(null) {

    private val accelerometerSubject = BehaviorSubject.create<Unit>()

    fun getAccelerometerObservable(): Observable<Unit> {
        return accelerometerSubject.serialize()
    }

    override fun onChange(selfChange: Boolean) {
        accelerometerSubject.onNext(Unit)
    }
}