package com.arttttt.rotationcontrolv3.ui.apps.platform

import android.os.Bundle
import android.view.View
import androidx.activity.result.launch
import androidx.fragment.app.Fragment
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.apps.controller.AppsController
import com.arttttt.rotationcontrolv3.ui.apps.di.AppsComponentDependencies
import com.arttttt.rotationcontrolv3.ui.apps.di.DaggerAppsComponent
import com.arttttt.rotationcontrolv3.ui.apps.view.AppsViewImpl
import com.arttttt.rotationcontrolv3.utils.resultcontracts.AccessibilityResultContract
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AppsFragment(
    private val dependencies: AppsComponentDependencies,
) : Fragment(R.layout.fragment_apps) {

    companion object {

        fun provider(dependencies: AppsComponentDependencies) = FragmentProvider {
            AppsFragment(
                dependencies = dependencies
            )
        }
    }

    private val accessibilityServiceFlow = MutableSharedFlow<Boolean>(
        extraBufferCapacity = 1,
    )

    private val accessibilityServiceLauncher = registerForActivityResult(
        AccessibilityResultContract(
            contextProvider = { requireContext() },
        )
    ) { result ->
        accessibilityServiceFlow.tryEmit(result)
    }

    @Inject
    lateinit var controller: AppsController

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppsComponent
            .factory()
            .create(
                dependencies = dependencies,
            )
            .inject(this)

        super.onCreate(savedInstanceState)

        controller.platformCallback = object : AppsController.PlatformCallback {
            override suspend fun launchAccessibilityService(): Boolean {
                accessibilityServiceLauncher.launch()

                return accessibilityServiceFlow.first()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller.onViewCreated(
            view = AppsViewImpl(view),
            lifecycle = viewLifecycleOwner.essentyLifecycle(),
        )
    }
}