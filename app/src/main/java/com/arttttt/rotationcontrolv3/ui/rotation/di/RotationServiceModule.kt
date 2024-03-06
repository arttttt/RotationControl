package com.arttttt.rotationcontrolv3.ui.rotation.di

import android.content.Context
import com.arttttt.rotationcontrolv3.data.repository.OrientationRepositoryImpl
import com.arttttt.rotationcontrolv3.data.repository.SensorsRepositoryImpl
import com.arttttt.rotationcontrolv3.di.scopes.PerService
import com.arttttt.rotationcontrolv3.domain.managers.ForcedOrientationManager
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.domain.stores.rotation.RotationStore
import com.arttttt.rotationcontrolv3.domain.stores.rotation.RotationStoreFactory
import com.arttttt.rotationcontrolv3.ui.rotation.PermissionsVerifier
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
            rotationStore: RotationStore,
        ): RotationServiceController {
            return RotationServiceController(
                rotationStore = rotationStore,
            )
        }

        @Provides
        @PerService
        fun providePermissionsVerifier(
            permissionsRepository: PermissionsRepository,
        ): PermissionsVerifier {
            return PermissionsVerifier(
                permissionsRepository = permissionsRepository,
            )
        }

        @Provides
        @PerService
        fun provideRotationStore(factory: RotationStoreFactory): RotationStore {
            return factory.create()
        }

        @Provides
        @PerService
        fun provideForcedOrientationManager(context: Context): ForcedOrientationManager {
            return ForcedOrientationManager(
                context = context,
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