@file:Suppress("NOTHING_TO_INLINE")

package com.arttttt.rotationcontrolv3.utils.extensions.android

import android.content.ContentResolver
import android.database.ContentObserver
import android.provider.Settings

inline fun ContentResolver.registerContentObserver(parameter: String, notifyForDescendants: Boolean, observer: ContentObserver) {
    registerContentObserver(
        Settings.System.getUriFor(parameter),
        notifyForDescendants,
        observer
    )
}

inline fun ContentResolver.putInt(name: String, value: Int) {
    Settings.System.putInt(this, name, value)
}