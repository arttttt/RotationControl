package com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings

import io.reactivex.Single

interface ICanWriteSettingsRequester {
    fun requestWriteSettingsPermission(): Single<Boolean>
}