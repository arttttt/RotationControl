package com.arttttt.rotationcontrolv3.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.ui.main.di.DaggerMainComponent
import com.arttttt.rotationcontrolv3.utils.behavior.BottomAppBarBehavior
import com.arttttt.rotationcontrolv3.utils.extensions.unsafeCastTo
import com.arttttt.navigation.MenuAppNavigator
import com.arttttt.navigation.factory.CustomFragmentFactory
import com.arttttt.navigation.factory.FragmentProvider
import com.arttttt.rotationcontrolv3.ui.main.di.MainComponentDependencies
import com.arttttt.rotationcontrolv3.utils.navigation.NavigationContainerDelegate
import com.arttttt.rotationcontrolv3.utils.navigationdialog.NavigationDialog
import com.github.terrakok.cicerone.NavigatorHolder
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import javax.inject.Inject

class MainFragment(
    private val dependencies: MainComponentDependencies,
) : Fragment(R.layout.fragment_main) {

    sealed class MenuItem : NavigationDialog.Item {

        data object Settings : MenuItem() {

            override val id: Int = R.id.item_settings
            override val title: CharSequence = "Settings"
        }

        data object About : MenuItem() {

            override val id: Int = R.id.item_about
            override val title: CharSequence = "About"
        }
    }

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

    @Inject
    lateinit var fragmentFactory: CustomFragmentFactory

    /**
     * todo: do it in a proper way
     */
    private var selectedMenuItem: NavigationDialog.Item = MenuItem.Settings

    override fun onCreate(savedInstanceState: Bundle?) {
        containerDelegate.initialize(savedInstanceState)

        DaggerMainComponent
            .factory()
            .create(
                dependencies = dependencies,
            )
            .inject(this)

        childFragmentManager.fragmentFactory = fragmentFactory

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

    private var started = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setImageResource(R.drawable.ic_start)

        fab.setOnClickListener {
            started = !started

            if (started) {
                fab.setImageResource(R.drawable.ic_stop)
            } else {
                fab.setImageResource(R.drawable.ic_start)
            }
        }

        val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottomAppBar)
        bottomAppBar.setNavigationOnClickListener {
            NavigationDialog.show(
                context = requireContext(),
                items = setOf(
                    MenuItem.Settings,
                    MenuItem.About,
                ),
                itemClickListener = { item ->
                    coordinator.handleMenuClick(item)
                    selectedMenuItem = item

                    when (item) {
                        is MenuItem.Settings -> fab.show()
                        is MenuItem.About -> fab.hide()
                    }
                },
                selectedItem = selectedMenuItem,
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

    private fun MainCoordinator.handleMenuClick(item: NavigationDialog.Item) {
        when (item) {
            is MenuItem.About -> showAbout()
            is MenuItem.Settings -> showSettings()
        }
    }
}