package com.arttttt.rotationcontrolv3.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.arttttt.rotationcontrolv3.domain.entity.AppInfo
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import javax.inject.Inject

class AppsRepositoryImpl @Inject constructor(
    private val context: Context,
) : AppsRepository {

    private val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    private val homeIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }

    override suspend fun getInstalledApps(): List<AppInfo> {
        val pm = context.packageManager

        val result = buildMap {
            pm
                .queryIntentActivities(launcherIntent, PackageManager.MATCH_DEFAULT_ONLY)
                .forEach { info ->
                    if (!containsKey(info.activityInfo.applicationInfo.packageName)) {
                        put(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.applicationInfo,
                        )
                    }
                }

            pm
                .queryIntentActivities(homeIntent, PackageManager.MATCH_DEFAULT_ONLY)
                .forEach { info ->
                    if (!containsKey(info.activityInfo.applicationInfo.packageName)) {
                        put(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.applicationInfo,
                        )
                    }
                }
        }

        return result
            .mapNotNull { (_, info) ->
                val title = pm
                    .getApplicationLabel(info)
                    .takeIf { it.isNotEmpty() }
                    ?.toString()
                    ?: return@mapNotNull null

                AppInfo(
                    title = title,
                    pkg = info.packageName,
                )
            }
            .sortedBy { info -> info.title }
    }
}