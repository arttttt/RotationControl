package com.arttttt.rotationcontrolv3.ui.rotation.di

import com.arttttt.rotationcontrolv3.data.repository.OrientationRepositoryImpl
import com.arttttt.rotationcontrolv3.data.repository.SensorsRepositoryImpl
import com.arttttt.rotationcontrolv3.di.scopes.PerService
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.ui.rotation.RotationServiceController
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
abstract class RotationServiceModule {

    companion object {

        @Provides
        @PerService
        fun provideRotationServiceController(
            sensorsRepository: SensorsRepository,
            orientationRepository: OrientationRepository,
        ): RotationServiceController {
            return RotationServiceController(
                sensorsRepository = sensorsRepository,
                orientationRepository = orientationRepository,
            )
        }
    }

    @Binds
    @PerService
    abstract fun bindSensorsRepository(impl: SensorsRepositoryImpl): SensorsRepository

    @Binds
    @PerService
    abstract fun bindsOrientationRepository(impl: OrientationRepositoryImpl): OrientationRepository
}