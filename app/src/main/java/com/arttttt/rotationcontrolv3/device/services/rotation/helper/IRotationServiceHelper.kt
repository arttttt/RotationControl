package com.arttttt.rotationcontrolv3.device.services.rotation.helper

import io.reactivex.Observable

interface IRotationServiceHelper {
    enum class Status {
        STOPPED,
        STARTED
    }

    fun getStatusObservable(): Observable<Status>
    fun startRotationService()
    fun stopRotationService()
    fun restartRotationService()
}