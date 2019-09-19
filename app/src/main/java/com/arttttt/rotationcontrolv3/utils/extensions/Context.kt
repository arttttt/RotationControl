package com.arttttt.rotationcontrolv3.utils.extensions

import android.content.Intent

fun intentOf(action: String, block: Intent.() -> Unit): Intent {
    return Intent(action).apply(block)
}