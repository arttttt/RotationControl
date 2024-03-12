package com.arttttt.rotationcontrolv3.framework.services.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository

interface AppOrientationAccessibilityServiceDependencies {

    val storeFactory: StoreFactory
    val appsRepository: AppsRepository
}