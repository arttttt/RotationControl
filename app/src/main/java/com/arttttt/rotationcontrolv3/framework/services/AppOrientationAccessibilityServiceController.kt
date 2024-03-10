package com.arttttt.rotationcontrolv3.framework.services

import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import javax.inject.Inject

class AppOrientationAccessibilityServiceController @Inject constructor(
    private val appsStore: AppsStore,
)