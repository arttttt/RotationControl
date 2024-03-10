package com.arttttt.rotationcontrolv3.domain.stores.apps

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.rotationcontrolv3.domain.entity.apps.AppEvent
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * todo: provide dispatchers provider
 */
class AppsStoreExecutor(
    private val appsRepository: AppsRepository,
) : CoroutineExecutor<AppsStore.Intent, AppsStore.Action, AppsStore.State, AppsStore.Message, AppsStore.Label>() {

    override fun executeAction(action: AppsStore.Action) {
        when (action) {
            is AppsStore.Action.LoadApps -> loadApps()
            is AppsStore.Action.SubscribeForAppsChanges -> subscribeForAppsChanges()
        }
    }

    override fun executeIntent(intent: AppsStore.Intent) {
        when (intent) {
            is AppsStore.Intent.LoadApps -> {
                loadApps()
                subscribeForAppsChanges()
            }
        }
    }

    private fun subscribeForAppsChanges() {
        appsRepository
            .getAppEventsFlow()
            .onEach(::handleAppEvent)
            .launchIn(scope)
    }

    private fun loadApps() {
        scope.launch {
            dispatch(AppsStore.Message.ProgressStarted)

            dispatch(
                AppsStore.Message.AppsReceived(
                    apps = withContext(Dispatchers.IO) {
                        appsRepository
                            .getInstalledApps()
                            .sortedBy { info -> info.title }
                    }
                )
            )

            dispatch(AppsStore.Message.ProgressFinished)
        }
    }

    private suspend fun handleAppEvent(event: AppEvent) {
        val mutableApps = state().apps.toMutableList()

        val updatedApps = withContext(Dispatchers.IO) {
            val apps = when (event) {
                is AppEvent.AppRemoved -> {
                    mutableApps.filter { info ->
                        info.pkg != event.appInfo.pkg
                    }
                }
                is AppEvent.AppInstalled -> {
                    mutableApps.apply {
                        add(event.appInfo)
                    }
                }
            }

            apps.sortedBy { info -> info.title }
        }

        dispatch(
            AppsStore.Message.AppsReceived(
                apps = updatedApps
            )
        )
    }
}