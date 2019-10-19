package com.arttttt.rotationcontrolv3.utils.delegates.permissions.drawoverlays

import io.reactivex.Single

interface ICanDrawOverlayRequester {
    fun requestDrawOverlayPermission(): Single<Boolean>
}