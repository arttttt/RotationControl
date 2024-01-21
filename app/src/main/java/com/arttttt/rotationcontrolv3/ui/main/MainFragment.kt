package com.arttttt.rotationcontrolv3.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.Screens
import com.arttttt.rotationcontrolv3.ui.settings.SettingsFragment
import com.arttttt.rotationcontrolv3.utils.behavior.BottomAppBarBehavior
import com.arttttt.rotationcontrolv3.utils.extensions.unsafeCastTo
import com.arttttt.rotationcontrolv3.utils.navigation.MenuAppNavigator
import com.arttttt.rotationcontrolv3.utils.navigation.MenuRouter
import com.arttttt.rotationcontrolv3.utils.navigation.NavigationContainerDelegate
import com.arttttt.rotationcontrolv3.utils.navigationdialog.NavigationDialog
import com.github.terrakok.cicerone.Cicerone
import com.google.android.material.bottomappbar.BottomAppBar

class MainFragment : Fragment(R.layout.fragment_main) {

    private val containerDelegate by lazy {
        NavigationContainerDelegate(
            context = requireContext(),
        )
    }

    private val cicerone by lazy {
        Cicerone.create(MenuRouter())
    }
    private val navigator by lazy {
        MenuAppNavigator(
            activity = requireActivity(),
            containerId = containerDelegate.containerId,
            fragmentManager = childFragmentManager,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        containerDelegate.initialize(savedInstanceState)

        super.onCreate(savedInstanceState)

        cicerone.router.showScreen(Screens.SettingsScreen())
    }

    @Suppress("NAME_SHADOWING")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState) ?: return null

        val container = containerDelegate.createContainerView()
        view.unsafeCastTo<ViewGroup>().addView(container)
        container.updateLayoutParams<CoordinatorLayout.LayoutParams> {
            behavior = BottomAppBarBehavior()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.commit {
            replace<SettingsFragment>(
                containerDelegate.containerId,
                null,
                null,
            )
        }

        val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar.setNavigationOnClickListener {
            NavigationDialog.show(
                context = requireContext(),
                itemClickListener = { itemId ->
                    when (itemId) {
                        R.id.item_about -> {
                            cicerone.router.showScreen(Screens.AboutScreen())
                        }
                        R.id.item_settings -> {
                            cicerone.router.showScreen(Screens.SettingsScreen())
                        }
                    }
                },
            )
        }
    }

    override fun onResume() {
        super.onResume()

        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()

        cicerone.getNavigatorHolder().removeNavigator()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        containerDelegate.saveState(outState)
    }
}