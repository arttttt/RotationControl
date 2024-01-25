package com.arttttt.rotationcontrolv3.utils.resources

import androidx.annotation.StringRes

interface ResourcesProvider {

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String
}