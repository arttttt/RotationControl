package com.arttttt.rotationcontrolv3.di.modules

import android.content.Context
import com.arttttt.rotationcontrolv3.data.repository.OrientationRepositoryImpl
import com.arttttt.rotationcontrolv3.data.repository.SensorsRepositoryImpl
import com.arttttt.rotationcontrolv3.domain.managers.ForcedOrientationManager
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.domain.stores.rotation.RotationStore
import com.arttttt.rotationcontrolv3.domain.stores.rotation.RotationStoreFactory
import com.arttttt.rotationcontrolv3.ui.rotation.PermissionsVerifier
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class RotationModule {
    
    companion object {

        @Provides
        @Singleton
        fun providePermissionsVerifier(
            permissionsRepository: PermissionsRepository,
            settingsRepository: SettingsRepository,
        ): PermissionsVerifier {
            return PermissionsVerifier(
                permissionsRepository = permissionsRepository,
                settingsRepository = settingsRepository,
            )
        }

        @Provides
        @Singleton
        fun provideRotationStore(factory: RotationStoreFactory): RotationStore {
            return factory.create()
        }

        @Provides
        @Singleton
        fun provideForcedOrientationManager(context: Context): ForcedOrientationManager {
            return ForcedOrientationManager(
                context = context,
            )
        }
    }

    @Binds
    @Singleton
    abstract fun bindSensorsRepository(impl: SensorsRepositoryImpl): SensorsRepository

    @Binds
    @Singleton
    abstract fun bindsOrientationRepository(impl: OrientationRepositoryImpl): OrientationRepository
}