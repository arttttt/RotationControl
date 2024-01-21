package com.arttttt.rotationcontrolv3.ui.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.arttttt.rotationcontrolv3.ui.main.MainFragment
import com.arttttt.rotationcontrolv3.utils.navigation.NavigationContainerDelegate

class ContainerFragment : Fragment() {

    private val containerDelegate by lazy {
        NavigationContainerDelegate(
            context = requireContext(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        containerDelegate.initialize(savedInstanceState)

        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) return

        childFragmentManager.commit {
            replace<MainFragment>(
                containerDelegate.containerId,
                null,
                null,
            )
        }
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
}