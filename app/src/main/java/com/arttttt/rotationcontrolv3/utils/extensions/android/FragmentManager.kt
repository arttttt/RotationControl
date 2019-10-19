package com.arttttt.rotationcontrolv3.utils.extensions.android

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

inline fun FragmentManager.beginTransaction(block: FragmentTransaction.() -> Unit) {
    beginTransaction().apply(block)
}