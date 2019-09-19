package com.arttttt.rotationcontrolv3.presentation.feature.mainmenu.view

import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.arttttt.rotationcontrolv3.R
import kotlinx.android.synthetic.main.fragment_mainmenu.*

class MainMenuFragment: BottomSheetDialogFragment() {

    companion object {
        const val navHostIdKey = "nav_host_id"
        const val tag = "main_menu"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mainmenu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var navHostId = 0

        arguments?.let { arguments ->
            navHostId = arguments.getInt(navHostIdKey, 0)
        }

        activity?.let { activity ->
            if (navHostId != 0)
                NavigationUI.setupWithNavController(navigation, Navigation.findNavController(activity, navHostId))
        }
    }
}