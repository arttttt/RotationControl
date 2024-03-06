package com.arttttt.rotationcontrolv3.ui.apps

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.apps.di.AppsComponentDependencies
import com.arttttt.rotationcontrolv3.ui.apps.di.DaggerAppsComponent
import com.arttttt.rotationcontrolv3.ui.apps.view.AppsViewImpl
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        controller.onViewCreated(
            view = AppsViewImpl(view),
            lifecycle = viewLifecycleOwner.essentyLifecycle(),
        )
    }
}