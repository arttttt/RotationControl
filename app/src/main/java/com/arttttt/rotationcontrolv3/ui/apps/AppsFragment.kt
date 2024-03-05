package com.arttttt.rotationcontrolv3.ui.apps

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.apps.di.AppsComponentDependencies
import com.arttttt.rotationcontrolv3.ui.apps.di.DaggerAppsComponent

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

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppsComponent
            .factory()
            .create(
                dependencies = dependencies,
            )
            .inject(this)

        super.onCreate(savedInstanceState)
    }
}