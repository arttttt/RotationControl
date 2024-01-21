package com.arttttt.rotationcontrolv3.utils.navigation

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentContainerView
import kotlin.properties.Delegates

class NavigationContainerDelegate(
    private val context: Context,
) {

    companion object {

        private const val CONTAINER_ID = "container_id"
        private const val NO_VIEW_ID = -1
    }

    var containerId: Int by Delegates.notNull()
        private set

    fun initialize(savedState: Bundle?) {
        containerId = savedState
            ?.getInt(CONTAINER_ID, NO_VIEW_ID)
            .takeIf { it != NO_VIEW_ID }
            ?: ViewCompat.generateViewId()
    }

    fun createContainerView(): View {
        return FragmentContainerView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )

            id = containerId
        }
    }

    fun saveState(outState: Bundle) {
        outState.putInt(CONTAINER_ID, containerId)
    }
}