package com.arttttt.rotationcontrolv3.framework.services.di

import com.arttttt.rotationcontrolv3.di.scopes.PerService
import com.arttttt.rotationcontrolv3.framework.services.AppOrientationAccessibilityService
import dagger.Component

@PerService
@Component(
    dependencies = [
        AppOrientationAccessibilityServiceDependencies::class,
    ]
)
interface AppOrientationAccessibilityServiceComponent {

    @Component.Factory
    interface Factory {

        fun create(
            dependencies: AppOrientationAccessibilityServiceDependencies
        ): AppOrientationAccessibilityServiceComponent
    }

    fun inject(service: AppOrientationAccessibilityService)
}