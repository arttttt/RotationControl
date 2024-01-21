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
import com.arttttt.rotationcontrolv3.ui.about.FragmentAbout
import com.arttttt.rotationcontrolv3.ui.settings.FragmentSettings
import com.arttttt.rotationcontrolv3.utils.BottomAppBarBehavior
import com.arttttt.rotationcontrolv3.utils.NavigationContainerDelegate
import com.arttttt.rotationcontrolv3.utils.extensions.unsafeCastTo
import com.arttttt.rotationcontrolv3.utils.navigationdialog.NavigationDialog
import com.google.android.material.bottomappbar.BottomAppBar

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.commit {
            replace<FragmentSettings>(
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
                            childFragmentManager.commit {
                                replace<FragmentAbout>(
                                    containerDelegate.containerId,
                                    null,
                                    null,
                                )
                            }
                        }
                        R.id.item_settings -> {
                            childFragmentManager.commit {
                                replace<FragmentSettings>(
                                    containerDelegate.containerId,
                                    null,
                                    null,
                                )
                            }
                        }
                    }
                },
            )
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        containerDelegate.saveState(outState)
    }
}