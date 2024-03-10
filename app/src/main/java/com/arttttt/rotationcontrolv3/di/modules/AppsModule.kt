package com.arttttt.rotationcontrolv3.di.modules

import android.content.Context
import com.arttttt.rotationcontrolv3.data.db.AppsDatabase
import com.arttttt.rotationcontrolv3.data.db.AppsOrientationDao
import com.arttttt.rotationcontrolv3.data.repository.AppsRepositoryImpl
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStore
import com.arttttt.rotationcontrolv3.domain.stores.apps.AppsStoreFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
abstract class AppsModule {

    companion object {

        @Provides
        @Singleton
        fun provideAppsStore(
            factory: AppsStoreFactory,
        ): AppsStore {
            return factory.create()
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
    abstract fun bindAppsRepository(impl: AppsRepositoryImpl): AppsRepository
}