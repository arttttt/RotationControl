package com.arttttt.rotationcontrolv3.utils.delegates.permissions.drawoverlays

import io.reactivex.Single

interface ICanDrawOverlayChecker {
    fun canDrawOverlay(): Single<Boolean>
}