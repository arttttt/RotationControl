package com.arttttt.rotationcontrolv3.ui.container.di

import android.content.Context
import com.arttttt.rotationcontrolv3.utils.resources.ResourcesProvider

interface ContainerDependencies {

    val context: Context
    val resourcesProvider: ResourcesProvider
}