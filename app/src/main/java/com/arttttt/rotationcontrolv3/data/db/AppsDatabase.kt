package com.arttttt.rotationcontrolv3.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        AppOrientationDbModel::class
    ],
    version = 1,
    exportSchema = false,
)
abstract class AppsDatabase : RoomDatabase() {

    companion object {

        fun create(context: Context): AppsDatabase {
            return Room
                .databaseBuilder(
                    context = context,
                    klass = AppsDatabase::class.java,
                    name = "apps_database",
                )
                .build()
        }
    }

    abstract fun getAppsOrientationDao(): AppsOrientationDao
}