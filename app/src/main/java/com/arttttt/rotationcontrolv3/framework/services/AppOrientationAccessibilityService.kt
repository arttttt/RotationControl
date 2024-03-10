package com.arttttt.rotationcontrolv3.framework.services

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arttttt.rotationcontrolv3.framework.services.di.DaggerAppOrientationAccessibilityServiceComponent
import com.arttttt.rotationcontrolv3.utils.extensions.appComponent
import timber.log.Timber
import javax.inject.Inject

class AppOrientationAccessibilityService : AccessibilityService() {

    private val lifecycle = LifecycleRegistry()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            Timber.e(event.packageName.toString())
        }
    }

    @Inject
    lateinit var controller: AppOrientationAccessibilityServiceController

    override fun onInterrupt() {}

    override fun onCreate() {
        DaggerAppOrientationAccessibilityServiceComponent
            .factory()
            .create(
                dependencies = applicationContext.appComponent,
            )
            .inject(this)

        super.onCreate()

        lifecycle.resume()
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycle.destroy()
    }
}