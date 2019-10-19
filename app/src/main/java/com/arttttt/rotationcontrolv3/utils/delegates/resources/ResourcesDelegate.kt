package com.arttttt.rotationcontrolv3.utils.delegates.resources

import android.content.res.Resources

class ResourcesDelegate(
    private val resources: Resources
): IResourcesDelegate {
    override fun getString(stringRes: Int): String {
        return resources.getString(stringRes)
    }

    override fun getString(stringRes: Int, vararg formatArgs: Any): String {
        return resources.getString(stringRes, *formatArgs)
    }
}