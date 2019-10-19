@file:Suppress("NOTHING_TO_INLINE")

package com.arttttt.rotationcontrolv3.utils.extensions.koilin

inline fun<reified R>Any?.castTo(): R? {
    return this as? R
}

inline fun<reified R>Any.unsafeCastTo(): R {
    return this as R
}

inline fun<T> T.toNonNull(): T {
    return this!!
}

inline fun<T>T.isNull(): Boolean {
    return this == null
}

inline fun<T>T.isNotNull(): Boolean {
    return this != null
}