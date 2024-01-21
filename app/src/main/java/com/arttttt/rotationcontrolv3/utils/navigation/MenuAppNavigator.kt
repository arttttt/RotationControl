package com.arttttt.rotationcontrolv3.utils.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.androidx.AppNavigator

class MenuAppNavigator(
    activity: FragmentActivity,
    containerId: Int,
    fragmentManager: FragmentManager = activity.supportFragmentManager,
    fragmentFactory: FragmentFactory = fragmentManager.fragmentFactory
) : AppNavigator(activity, containerId, fragmentManager, fragmentFactory) {

    override fun applyCommand(command: Command) {
        when (command) {
            is Show -> show(command)
            else -> super.applyCommand(command)
        }
    }

    private fun show(command: Show) {
        val currentFragment = fragmentManager.fragments.firstOrNull(Fragment::isVisible)
        val savedFragment = fragmentManager.findFragmentByTag(command.screen.screenKey)

        val newFragment = savedFragment ?: command.screen.createFragment(fragmentFactory)

        if (currentFragment === newFragment) {
            return
        }

        fragmentManager.commit {
            currentFragment?.let { fragment -> hide(fragment) }
            savedFragment?.let { fragment -> show(fragment) } ?: add(containerId, newFragment, command.screen.screenKey)
        }
    }
}