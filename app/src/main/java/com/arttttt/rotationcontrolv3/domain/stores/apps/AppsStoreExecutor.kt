package com.arttttt.rotationcontrolv3.domain.stores.apps

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
import kotlinx.coroutines.Dispatchers
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
        }
    }

    override fun executeIntent(intent: AppsStore.Intent) {
        super.executeIntent(intent)
    }

    private fun loadApps() {
        scope.launch {
            dispatch(AppsStore.Message.ProgressStarted)

            dispatch(
                AppsStore.Message.AppsLoaded(
                    apps = withContext(Dispatchers.IO) {
                        appsRepository.getInstalledApps()
                    }
                )
            )

            dispatch(AppsStore.Message.ProgressFinished)
        }
    }
}