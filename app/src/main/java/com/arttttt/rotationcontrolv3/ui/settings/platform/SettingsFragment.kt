package com.arttttt.rotationcontrolv3.ui.settings.platform

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.arkivanov.essenty.lifecycle.essentyLifecycle
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.settings.controller.SettingsController
import com.arttttt.rotationcontrolv3.ui.settings.di.DaggerSettingsComponent
import com.arttttt.rotationcontrolv3.ui.settings.di.SettingsDependencies
import com.arttttt.rotationcontrolv3.ui.settings.view.SettingsViewImpl
import javax.inject.Inject

class SettingsFragment(
    private val dependencies: SettingsDependencies,
) : Fragment(R.layout.fragment_settings) {

    companion object {

        fun provider(dependencies: SettingsDependencies) = FragmentProvider {
            SettingsFragment(dependencies)
        }
    }

    @Inject
    lateinit var controller: SettingsController

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerSettingsComponent
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
            view = SettingsViewImpl(view),
            lifecycle = viewLifecycleOwner.essentyLifecycle(),
        )
    }
}