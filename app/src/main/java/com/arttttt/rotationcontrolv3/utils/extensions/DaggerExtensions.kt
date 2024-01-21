package com.arttttt.rotationcontrolv3.utils.extensions

import android.content.Context
import android.content.ContextWrapper
import com.arttttt.rotationcontrolv3.di.AppComponent
import com.arttttt.rotationcontrolv3.utils.di.AppComponentOwner

val Context.appComponent: AppComponent
    get() {
        return when (this) {
            is AppComponentOwner -> component
            is ContextWrapper -> baseContext.applicationContext.appComponent
            else -> throw IllegalStateException("can't find app component")
        }
    }