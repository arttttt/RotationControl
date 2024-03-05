package com.arttttt.rotationcontrolv3.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.arttttt.rotationcontrolv3.domain.entity.AppInfo
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository

class AppsRepositoryImpl(
    private val context: Context,
) : AppsRepository {

    companion object {

        private const val FLAGS = 128
    }

    private val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    private val homeIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }

    @SuppressLint("QueryPermissionsNeeded")
    override suspend fun getInstalledApps(): List<AppInfo> {
        val pm = context.packageManager

        val result = buildSet {
            addAll(pm.queryIntentActivities(launcherIntent, FLAGS))
            addAll(pm.queryIntentActivities(homeIntent, FLAGS))
        }

        return result.mapNotNull { info ->
            val title = pm
                .getApplicationLabel(info.activityInfo.applicationInfo)
                .takeIf { it.isNotEmpty() }
                ?.toString()
                ?: return@mapNotNull null

            AppInfo(
                title = title,
                pkg = info.activityInfo.applicationInfo.packageName,
            )
        }
    }
}