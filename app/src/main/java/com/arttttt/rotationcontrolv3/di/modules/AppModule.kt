package com.arttttt.rotationcontrolv3.di.modules

import android.content.Context
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.arttttt.rotationcontrolv3.data.db.AppsDatabase
import com.arttttt.rotationcontrolv3.data.db.AppsOrientationDao
import com.arttttt.rotationcontrolv3.data.repository.AppsRepositoryImpl
import com.arttttt.rotationcontrolv3.data.repository.PermissionsRepositoryImpl
import com.arttttt.rotationcontrolv3.data.repository.SettingsRepositoryImpl
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import com.arttttt.rotationcontrolv3.domain.repository.PermissionsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.domain.stores.settings.SettingsStore
import com.arttttt.rotationcontrolv3.domain.stores.settings.SettingsStoreFactory
import com.arttttt.rotationcontrolv3.utils.resources.ResourceProviderImpl
import com.arttttt.rotationcontrolv3.utils.resources.ResourcesProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppModule {

    companion object {

        @Provides
        @Singleton
        fun provideSettingsStore(
            storeFactory: StoreFactory,
            settingsRepository: SettingsRepository,
        ): SettingsStore {
            return SettingsStoreFactory(
                settingsRepository = settingsRepository,
                storeFactory = storeFactory
            ).create()
        }

        @Provides
        fun provideStoreFactory(): StoreFactory {
            return LoggingStoreFactory(
                delegate = DefaultStoreFactory(),
            )
        }

        @Provides
        @Singleton
        fun provideAppsDatabase(context: Context): AppsDatabase {
            return AppsDatabase.create(
                context = context,
            )
        }

        @Provides
        @Singleton
        fun provideAppsOrientationDao(db: AppsDatabase): AppsOrientationDao {
            return db.getAppsOrientationDao()
        }
    }

    @Binds
    @Singleton
    abstract fun bindResourcesProvider(impl: ResourceProviderImpl): ResourcesProvider

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindPermissionsRepository(impl: PermissionsRepositoryImpl): PermissionsRepository

    @Binds
    @Singleton
    abstract fun bindAppsRepository(impl: AppsRepositoryImpl): AppsRepository
}