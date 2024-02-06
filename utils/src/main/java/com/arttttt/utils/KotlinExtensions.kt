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

fun Int.toBoolean(): Boolean {
    return when (this) {
        0 -> false
        1 -> true
        else -> throw IllegalStateException("can't convert to Boolean: $this")
    }
}