package com.arttttt.rotationcontrolv3.ui.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.arttttt.rotationcontrolv3.ui.container.di.DaggerContainerComponent
import com.arttttt.rotationcontrolv3.utils.navigation.NavigationContainerDelegate
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

class ContainerFragment : Fragment() {

    private val containerDelegate by lazy {
        NavigationContainerDelegate(
            context = requireContext(),
        )
    }

    private val cicerone by lazy {
        Cicerone.create()
    }

    private val navigator by lazy {
        AppNavigator(
            activity = requireActivity(),
            containerId = containerDelegate.containerId,
            fragmentManager = childFragmentManager,
        )
    }

    @Inject
    lateinit var coordinator: ContainerCoordinator

    override fun onCreate(savedInstanceState: Bundle?) {
        containerDelegate.initialize(savedInstanceState)

        DaggerContainerComponent
            .factory()
            .create(
                router = cicerone.router
            )
            .inject(this)

        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) return

        coordinator.start()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return containerDelegate.createContainerView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        containerDelegate.saveState(outState)
    }

    override fun onResume() {
        super.onResume()

        cicerone.getNavigatorHolder().setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()

        cicerone.getNavigatorHolder().removeNavigator()
    }
}