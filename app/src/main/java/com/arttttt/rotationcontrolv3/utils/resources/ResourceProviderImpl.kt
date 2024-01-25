package com.arttttt.rotationcontrolv3.utils.resources

import android.content.Context
import javax.inject.Inject

class ResourceProviderImpl @Inject constructor(
    private val context: Context,
) : ResourcesProvider {

    override fun getString(resId: Int, vararg formatArgs: Any): String {
        return if (formatArgs.isEmpty()) {
            context.getString(resId)
        } else {
            context.getString(resId, formatArgs)
        }
    }
}