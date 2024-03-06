package com.arttttt.rotationcontrolv3.ui.apps.di

import android.content.Context
import com.arkivanov.mvikotlin.core.store.StoreFactory

interface AppsComponentDependencies {

    val context: Context
    val storeFactory: StoreFactory
}