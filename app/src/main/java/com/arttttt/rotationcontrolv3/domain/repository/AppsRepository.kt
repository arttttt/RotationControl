package com.arttttt.rotationcontrolv3.domain.repository

import com.arttttt.rotationcontrolv3.domain.entity.apps.AppEvent
import com.arttttt.rotationcontrolv3.domain.entity.apps.AppInfo
import kotlinx.coroutines.flow.Flow

interface AppsRepository {

    suspend fun getInstalledApps(): List<AppInfo>

    fun getAppEventsFlow(): Flow<AppEvent>
}