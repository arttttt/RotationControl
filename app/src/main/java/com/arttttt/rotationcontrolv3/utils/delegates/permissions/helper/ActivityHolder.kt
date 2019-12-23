package com.arttttt.rotationcontrolv3.utils.delegates.permissions.helper

import com.arttttt.rotationcontrolv3.presentation.AppActivity

interface ActivityHolder {
    fun attachActivity(activity: AppActivity)
    fun detachActivity()
}