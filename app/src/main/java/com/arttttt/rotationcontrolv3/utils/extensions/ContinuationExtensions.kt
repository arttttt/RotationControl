package com.arttttt.rotationcontrolv3.utils.extensions

import kotlinx.coroutines.CancellableContinuation
import kotlin.coroutines.resume

fun <T> CancellableContinuation<T>.resumeWhenActive(value: T) {
    if (!isActive) return

    resume(value)
}