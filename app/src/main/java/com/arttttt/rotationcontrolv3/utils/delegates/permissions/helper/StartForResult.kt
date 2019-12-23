package com.arttttt.rotationcontrolv3.utils.delegates.permissions.helper

import io.reactivex.Single

interface StartForResult {
    fun startForResult(action: String, requestCode: Int): Single<Int>
}