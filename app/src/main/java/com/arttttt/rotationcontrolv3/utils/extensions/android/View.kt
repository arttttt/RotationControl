package com.arttttt.rotationcontrolv3.utils.extensions.android

import android.widget.ImageView
import io.reactivex.functions.Consumer

fun ImageView.imageResource(): Consumer<Int> {
    return Consumer { imageRes -> setImageResource(imageRes) }
}