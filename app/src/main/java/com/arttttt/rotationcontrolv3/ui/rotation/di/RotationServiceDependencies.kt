package com.arttttt.rotationcontrolv3.ui.rotation.di

import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository

interface RotationServiceDependencies {

    val sensorsRepository: SensorsRepository
}