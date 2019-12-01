package com.arttttt.rotationcontrolv3.utils

import android.content.Context
import android.view.View

class ViewProvider(
    private val context: Context
): IViewProvider {
    override fun provideView(): View {
        return View(context)
    }
}