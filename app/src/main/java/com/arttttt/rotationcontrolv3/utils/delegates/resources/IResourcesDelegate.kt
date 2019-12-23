package com.arttttt.rotationcontrolv3.utils.delegates.resources

interface IResourcesDelegate {
    fun getString(stringRes: Int, vararg formatArgs: Any): String
}