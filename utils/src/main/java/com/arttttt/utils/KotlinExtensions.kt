package com.arttttt.utils

import kotlinx.coroutines.CancellationException

inline fun <reified R> Any.castTo(): R? {
    return this as? R
}

inline fun <reified R> Any.unsafeCastTo(): R {
    return this as R
}

inline fun <T> Result<T>.finally(block: () -> Unit): Result<T> {
    return try {
        this
    } finally {
        block.invoke()
    }
}

fun <T> Result<T>.exceptCancellationException(): Result<T> {
    return onFailure { e ->
        if (e is CancellationException) throw e
    }
}