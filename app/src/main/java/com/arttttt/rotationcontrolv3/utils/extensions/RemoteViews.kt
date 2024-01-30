package com.arttttt.rotationcontrolv3.utils.extensions

import android.widget.RemoteViews

fun RemoteViews.setBackgroundResource(viewId: Int, resId: Int) {
    setInt(viewId, "setBackgroundResource", resId)
}

fun RemoteViews.setColorFilter(viewId: Int, colorRes: Int) {
    setInt(viewId, "setColorFilter", colorRes)
}