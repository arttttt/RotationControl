package com.arttttt.rotationcontrolv3.framework.services.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore

interface AppOrientationAccessibilityServiceDependencies {

    val appsStore: AppsStore
    val storeFactory: StoreFactory
    val appsRepository: AppsRepository
}