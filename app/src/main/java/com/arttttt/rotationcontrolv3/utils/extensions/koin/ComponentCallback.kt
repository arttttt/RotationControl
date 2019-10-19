package com.arttttt.rotationcontrolv3.utils.extensions.koin

import android.content.ComponentCallbacks
import org.koin.android.ext.android.getKoin

val ComponentCallbacks.koin
    get() = getKoin()