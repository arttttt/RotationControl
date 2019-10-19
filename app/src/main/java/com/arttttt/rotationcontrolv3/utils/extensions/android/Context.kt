@file:Suppress("NOTHING_TO_INLINE")

package com.arttttt.rotationcontrolv3.utils.extensions.android

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat

inline fun LayoutInflater.inflate(layoutRes: Int, block: View.() -> Unit = {}) {
    inflate(layoutRes, null).apply(block)
}

inline fun toastOf(context: Context, @StringRes textRes: Int, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, textRes, duration).show()
}

inline fun toastOf(context: Context, message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(context, message, duration).show()
}

inline fun Context.getColorCompat(@ColorRes colorRes: Int): Int {
    return ContextCompat.getColor(this, colorRes)
}