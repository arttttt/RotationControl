package com.arttttt.rotationcontrolv3.utils.extensions

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

inline fun <reified T : Fragment> FragmentFactory.instantiate(): Fragment {
    return instantiate(T::class.java.classLoader!!, T::class.java.name)
}