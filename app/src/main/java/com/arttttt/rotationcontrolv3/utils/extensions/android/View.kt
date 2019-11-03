package com.arttttt.rotationcontrolv3.utils.extensions.android

import android.view.View
import android.widget.ImageView
import io.reactivex.functions.Consumer

val View.isVisible
    get() = visibility == View.VISIBLE

fun ImageView.imageResource(): Consumer<Int> {
    return Consumer { imageRes -> setImageResource(imageRes) }
}