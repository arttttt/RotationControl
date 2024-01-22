package com.arttttt.navigation.factory

import androidx.fragment.app.Fragment

fun interface FragmentProvider {

    fun provide(): Fragment
}