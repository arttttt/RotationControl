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
import com.arttttt.rotationcontrolv3.ui.main.di.DaggerMainComponent
import com.arttttt.rotationcontrolv3.ui.settings.SettingsFragment
import com.arttttt.rotationcontrolv3.utils.behavior.BottomAppBarBehavior
import com.arttttt.rotationcontrolv3.utils.extensions.unsafeCastTo
import com.arttttt.navigation.MenuAppNavigator
import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.rotationcontrolv3.ui.main.di.MainComponentDependencies
import com.arttttt.rotationcontrolv3.utils.navigation.NavigationContainerDelegate
import com.arttttt.rotationcontrolv3.utils.navigationdialog.NavigationDialog
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.google.android.material.bottomappbar.BottomAppBar
import javax.inject.Inject

class MainFragment(
    private val dependencies: MainComponentDependencies,
) : Fragment(R.layout.fragment_main) {

    companion object {

        fun provider(dependencies: MainComponentDependencies): FragmentProvider = FragmentProvider {
            MainFragment(dependencies)
        }
    }

    private val containerDelegate by lazy {
        NavigationContainerDelegate(
            context = requireContext(),
        )
    }

    private val navigator by lazy {
        MenuAppNavigator(
            activity = requireActivity(),
            containerId = containerDelegate.containerId,
            fragmentManager = childFragmentManager,
        )
    }

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var coordinator: MainCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        containerDelegate.initialize(savedInstanceState)

        DaggerMainComponent
            .factory()
            .create(
                dependencies = dependencies,
            )
            .inject(this)

        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) return

        coordinator.start()
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
                itemClickListener = { itemId -> coordinator.handleMenuClick(itemId) },
            )
        }
    }

    override fun onResume() {
        super.onResume()

        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()

        navigatorHolder.removeNavigator()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        containerDelegate.saveState(outState)
    }

    private fun MainCoordinator.handleMenuClick(itemId: Int) {
        when (itemId) {
            R.id.item_about -> showAbout()
            R.id.item_settings -> showSettings()
        }
    }
}