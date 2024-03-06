package com.arttttt.rotationcontrolv3.domain.repository

import com.arttttt.rotationcontrolv3.domain.entity.apps.AppInfo

interface AppsRepository {

    suspend fun getInstalledApps(): List<AppInfo>
}