@file:Suppress("NOTHING_TO_INLINE")

package com.arttttt.rotationcontrolv3.utils.extensions.android

import android.net.Uri

inline fun String.toUri(): Uri {
    return Uri.parse(this)
}