package com.arttttt.rotationcontrolv3.framework.services.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import com.arttttt.rotationcontrolv3.domain.stores.rotation.RotationStore

interface AppOrientationAccessibilityServiceDependencies {

    val storeFactory: StoreFactory
    val appsRepository: AppsRepository
    val rotationStore: RotationStore
}