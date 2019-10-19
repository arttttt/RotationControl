package com.arttttt.rotationcontrolv3.presentation.delegate.screenswitcher

import androidx.fragment.app.FragmentManager

interface IScreenSwitcherFragmentManagerHolder {
    fun attachFragmentManager(fragmentManager: FragmentManager)
    fun detachFragmentManager()
}