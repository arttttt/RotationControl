@file:Suppress("NOTHING_TO_INLINE")

package com.arttttt.rotationcontrolv3.utils.extensions.android

import android.widget.RemoteViews

inline fun RemoteViews.setBackgroundResource(viewId: Int, resId: Int) {
    setInt(viewId, "setBackgroundResource", resId)
}

inline fun RemoteViews.setColorFilter(viewId: Int, colorRes: Int) {
    setInt(viewId, "setColorFilter", colorRes)
}