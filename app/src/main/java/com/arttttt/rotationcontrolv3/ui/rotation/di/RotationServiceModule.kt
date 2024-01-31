package com.arttttt.rotationcontrolv3.ui.rotation.di

import com.arttttt.rotationcontrolv3.di.scopes.PerService
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.ui.rotation.RotationServiceController
import dagger.Module
import dagger.Provides

@Module
object RotationServiceModule {

    @Provides
    @PerService
    fun provideRotationServiceController(
        sensorsRepository: SensorsRepository,
    ): RotationServiceController {
        return RotationServiceController(
            sensorsRepository = sensorsRepository
        )
    }
}