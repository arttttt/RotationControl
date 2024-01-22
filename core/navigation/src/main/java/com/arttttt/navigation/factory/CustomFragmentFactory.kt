package com.arttttt.navigation.factory

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

class CustomFragmentFactory(
    private val providers: Map<Class<out Fragment>, FragmentProvider>
) : FragmentFactory() {
    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        val clazz = loadFragmentClass(classLoader, className)

        return providers[clazz]?.provide() ?: super.instantiate(classLoader, className)
    }
}