package com.arttttt.rotationcontrolv3.ui

import com.arttttt.rotationcontrolv3.ui.about.AboutFragment
import com.arttttt.rotationcontrolv3.ui.main.MainFragment
import com.arttttt.rotationcontrolv3.ui.settings.SettingsFragment
import com.arttttt.navigation.FragmentFactoryScreen

object Screens {

    fun MainScreen() = FragmentFactoryScreen<MainFragment>()

    fun SettingsScreen() = FragmentFactoryScreen<SettingsFragment>()

    fun AboutScreen() = FragmentFactoryScreen<AboutFragment>()
}