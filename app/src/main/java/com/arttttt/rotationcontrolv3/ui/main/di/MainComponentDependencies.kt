package com.arttttt.rotationcontrolv3.ui.main.di

import android.content.Context
import com.arttttt.navigation.FlowMenuRouter
import com.arttttt.rotationcontrolv3.di.qualifiers.RootRouterQualifier

interface MainComponentDependencies {

    @get:RootRouterQualifier
    val rootRouter: FlowMenuRouter

    val context: Context
}