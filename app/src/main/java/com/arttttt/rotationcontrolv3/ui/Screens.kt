package com.arttttt.rotationcontrolv3.ui

import android.content.Intent
import android.net.Uri
import com.arttttt.rotationcontrolv3.ui.about.AboutFragment
import com.arttttt.rotationcontrolv3.ui.main.MainFragment
import com.arttttt.rotationcontrolv3.ui.settings.platform.SettingsFragment
import com.arttttt.navigation.FragmentFactoryScreen
import com.arttttt.rotationcontrolv3.ui.main2.MainFragment2
import com.github.terrakok.cicerone.androidx.ActivityScreen

object Screens {

    fun MainScreen() = FragmentFactoryScreen<MainFragment>()

    fun MainScreen2() = FragmentFactoryScreen<MainFragment2>()

    fun SettingsScreen() = FragmentFactoryScreen<SettingsFragment>()

    fun AboutScreen() = FragmentFactoryScreen<AboutFragment>()

    fun UriScreen(
        uri: Uri
    ) = ActivityScreen {
        Intent(Intent.ACTION_VIEW, uri)
    }
}