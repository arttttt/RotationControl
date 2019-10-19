package com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings

import io.reactivex.Single

interface ICanWriteSettingsChecker {
    fun canWriteSettings(): Single<Boolean>
}