package com.arttttt.rotationcontrolv3.utils.delegates.errordispatcher

import timber.log.Timber

class ErrorDispatcher: IErrorDispatcher {
    override fun dispatchError(error: Throwable) {
        Timber.e(error)
    }
}