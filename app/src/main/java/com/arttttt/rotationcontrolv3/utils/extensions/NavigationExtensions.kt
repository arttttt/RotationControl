package com.arttttt.rotationcontrolv3.utils.extensions

import androidx.fragment.app.Fragment
import com.github.terrakok.cicerone.androidx.FragmentScreen

inline fun <reified T : Fragment> FragmentFactoryScreen(
    crossinline block: Fragment.() -> Unit = {}
): FragmentScreen {
    return FragmentScreen { factory ->
        factory.instantiate<T>().apply(block)
    }
}