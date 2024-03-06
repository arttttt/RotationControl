package com.arttttt.rotationcontrolv3.data.repository

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.arttttt.rotationcontrolv3.domain.entity.apps.AppEvent
import com.arttttt.rotationcontrolv3.domain.entity.apps.AppInfo
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@OptIn(DelicateCoroutinesApi::class)
class AppsRepositoryImpl @Inject constructor(
    context: Context,
) : AppsRepository {

    private val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_LAUNCHER)
    }

    private val homeIntent = Intent(Intent.ACTION_MAIN).apply {
        addCategory(Intent.CATEGORY_HOME)
    }

    private val pm = context.packageManager

    private val appEventsFlow = context
        .appChangesFlow()
        .shareIn(
            scope = GlobalScope,
            started = SharingStarted.WhileSubscribed(
                stopTimeoutMillis = 5000L,
            ),
        )

    override suspend fun getInstalledApps(): List<AppInfo> {
        return pm
            .getAppsMap()
            .mapNotNull { (_, info) -> info.toAppInfo(pm) }
    }

    override fun getAppEventsFlow(): Flow<AppEvent> {
        return appEventsFlow
    }

    private fun PackageManager.getAppsMap(): Map<String, ApplicationInfo> {
        return buildMap {
            this@getAppsMap
                .queryIntentActivities(launcherIntent, PackageManager.GET_META_DATA)
                .forEach { info ->
                    if (!containsKey(info.activityInfo.applicationInfo.packageName)) {
                        put(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.applicationInfo,
                        )
                    }
                }

            this@getAppsMap
                .queryIntentActivities(homeIntent, PackageManager.GET_META_DATA)
                .forEach { info ->
                    if (!containsKey(info.activityInfo.applicationInfo.packageName)) {
                        put(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.applicationInfo,
                        )
                    }
                }
        }
    }

    private fun ApplicationInfo.toAppInfo(pm: PackageManager): AppInfo? {
        val title = pm
            .getApplicationLabel(this)
            .takeIf { it.isNotEmpty() }
            ?.toString()
            ?: return null

        return AppInfo(
            title = title,
            pkg = this.packageName,
        )
    }

    private fun Context.appChangesFlow(): Flow<AppEvent> {
        return callbackFlow {
            val receiver = object : BroadcastReceiver() {
                override fun onReceive(
                    context: Context,
                    intent: Intent?,
                ) {
                    val packageName = intent?.data?.schemeSpecificPart ?: return

                    val title = try {
                        packageManager
                            .getApplicationInfo(packageName, 0)
                            .let(packageManager::getApplicationLabel)
                            .toString()
                    } catch (e: PackageManager.NameNotFoundException) {
                        "Unknown"
                    }

                    val appInfo = AppInfo(
                        title = title,
                        pkg = packageName,
                    )

                    when (intent.action) {
                        Intent.ACTION_PACKAGE_REMOVED -> {
                            trySend(
                                AppEvent.AppRemoved(
                                    appInfo = appInfo,
                                )
                            )
                        }
                        Intent.ACTION_PACKAGE_ADDED -> {
                            trySend(
                                AppEvent.AppInstalled(
                                    appInfo = appInfo,
                                )
                            )
                        }
                    }
                }
            }

            val intentFilter = IntentFilter().apply {
                addAction(Intent.ACTION_PACKAGE_REMOVED)
                addAction(Intent.ACTION_PACKAGE_ADDED)
                addDataScheme("package")
            }

            ContextCompat.registerReceiver(
                this@appChangesFlow,
                receiver,
                intentFilter,
                ContextCompat.RECEIVER_NOT_EXPORTED,
            )

            awaitClose {
                this@appChangesFlow.unregisterReceiver(receiver)
            }
        }
    }
}