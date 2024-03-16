package com.arttttt.rotationcontrolv3.utils.extensions

import android.content.res.Resources
import com.arttttt.rotationcontrolv3.utils.Dp
import kotlin.math.roundToInt

val Int.dp: Dp
    get() {
        return Dp(this)
    }

fun Dp.toPx(): Int {
    return (Resources.getSystem().displayMetrics.density * value).roundToInt()
}