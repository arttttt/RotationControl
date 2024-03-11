package com.arttttt.rotationcontrolv3.framework.services

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.essenty.lifecycle.resume
import com.arttttt.rotationcontrolv3.framework.services.di.DaggerAppOrientationAccessibilityServiceComponent
import com.arttttt.rotationcontrolv3.utils.extensions.appComponent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class AppOrientationAccessibilityService : AccessibilityService() {

    sealed interface AccessibilityServiceEvent {

        data class WindowChanged(
            val pkg: String,
        ) : AccessibilityServiceEvent
    }

    private val events = MutableSharedFlow<AccessibilityServiceEvent>(
        extraBufferCapacity = 1,
    )

    private val lifecycle = LifecycleRegistry()

    override fun onAccessibilityEvent(event: AccessibilityEvent) {
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            events.tryEmit(
                AccessibilityServiceEvent.WindowChanged(
                    pkg = event.packageName.toString(),
                )
            )
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

        controller.onCreated(
            lifecycle = lifecycle,
            events = events.asSharedFlow(),
        )

        lifecycle.resume()
    }

    override fun onDestroy() {
        super.onDestroy()

        lifecycle.destroy()
    }
}