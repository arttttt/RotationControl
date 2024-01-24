package com.arttttt.rotationcontrolv3.utils.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView

context(Fragment)
fun RecyclerView.clearAdapterOnDestroyView() {
    viewLifecycleOwner.lifecycle.doOnDestroy {
        adapter = null
    }
}

fun RecyclerView.clearAdapterOnDestroyView() {
    findViewTreeLifecycleOwner()?.lifecycle?.doOnDestroy {
        adapter = null
    }
}

inline fun Lifecycle.doOnDestroy(crossinline block: () -> Unit) {
    addObserver(
        object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                block.invoke()
            }
        }
    )
}