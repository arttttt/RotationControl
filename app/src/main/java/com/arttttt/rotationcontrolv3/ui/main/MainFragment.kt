package com.arttttt.rotationcontrolv3.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.utils.BottomAppBarBehavior
import com.arttttt.rotationcontrolv3.utils.NavigationContainerDelegate
import com.arttttt.rotationcontrolv3.utils.extensions.unsafeCastTo

class MainFragment : Fragment(R.layout.fragment_main) {

    private val containerDelegate by lazy {
        NavigationContainerDelegate(
            context = requireContext(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        containerDelegate.initialize(savedInstanceState)

        super.onCreate(savedInstanceState)
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        containerDelegate.saveState(outState)
    }
}