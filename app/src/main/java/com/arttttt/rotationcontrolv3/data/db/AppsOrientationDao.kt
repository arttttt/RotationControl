package com.arttttt.rotationcontrolv3.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppsOrientationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppOrientation(vararg appOrientation: AppOrientationDbModel)

    @Query("delete from app_orientation_table where pkg = :pkg")
    suspend fun deleteAppOrientation(pkg: String)

    @Query("select * from app_orientation_table where pkg = :pkg")
    suspend fun getAppOrientation(pkg: String): AppOrientationDbModel?
}