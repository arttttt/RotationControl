package com.arttttt.rotationcontrolv3.ui.apps.di

import android.content.Context
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository

interface AppsComponentDependencies {

    val context: Context
    val storeFactory: StoreFactory
    val appsRepository: AppsRepository
}