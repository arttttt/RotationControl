package com.arttttt.rotationcontrolv3.utils.behavior

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.bottomappbar.BottomAppBar

class BottomAppBarBehavior @JvmOverloads constructor(
    context: Context? = null,
    attrs: AttributeSet? = null,
): CoordinatorLayout.Behavior<View>(context, attrs) {
    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return dependency is BottomAppBar
    }

    override fun onMeasureChild(
        parent: CoordinatorLayout,
        child: View,
        parentWidthMeasureSpec: Int,
        widthUsed: Int,
        parentHeightMeasureSpec: Int,
        heightUsed: Int
    ): Boolean {
        val childHeight = child.layoutParams.height

        if (childHeight != ViewGroup.LayoutParams.MATCH_PARENT && childHeight != ViewGroup.LayoutParams.WRAP_CONTENT) return false

        val header = parent
            .getDependencies(child)
            .let { dependencies -> findHeader(dependencies) }
            ?: return false

        if (ViewCompat.getFitsSystemWindows(header) && !ViewCompat.getFitsSystemWindows(child)) {
            child.fitsSystemWindows = true
            if (ViewCompat.getFitsSystemWindows(child)) {
                child.requestLayout()
                return true
            }
        }

        var availableHeight = View.MeasureSpec.getSize(parentHeightMeasureSpec)
        if (availableHeight == 0) {
            availableHeight = parent.height
        }

        val heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(
            availableHeight - header.measuredHeight,
            if (childHeight == ViewGroup.LayoutParams.MATCH_PARENT)
                View.MeasureSpec.EXACTLY
            else
                View.MeasureSpec.AT_MOST
        )
        parent.onMeasureChild(
            child,
            parentWidthMeasureSpec,
            widthUsed,
            heightMeasureSpec,
            heightUsed
        )

        return true
    }

    private fun findHeader(dependencies: List<View>): View? {
        return dependencies.firstOrNull { view -> view is BottomAppBar }
    }
}