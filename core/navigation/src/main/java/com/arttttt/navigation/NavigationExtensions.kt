package com.arttttt.navigation

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.github.terrakok.cicerone.androidx.FragmentScreen

inline fun <reified T : Fragment> FragmentFactory.instantiate(): Fragment {
    return instantiate(T::class.java.classLoader!!, T::class.java.name)
}

inline fun <reified T : Fragment> FragmentFactoryScreen(
    crossinline block: Fragment.() -> Unit = {}
): FragmentScreen {
    return FragmentScreen { factory ->
        factory.instantiate<T>().apply(block)
    }
}