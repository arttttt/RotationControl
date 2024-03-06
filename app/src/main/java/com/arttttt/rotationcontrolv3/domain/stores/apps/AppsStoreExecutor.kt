package com.arttttt.rotationcontrolv3.domain.stores.apps
 
 import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
 import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository

/**
  * todo: provide dispatchers provider
  */
 class AppsStoreExecutor(
     private val appsRepository: AppsRepository,
 ) : CoroutineExecutor<AppsStore.Intent, AppsStore.Action, AppsStore.State, AppsStore.Message, AppsStore.Label>() {
 
     override fun executeAction(action: AppsStore.Action) {
         super.executeAction(action)
     }
 
     override fun executeIntent(intent: AppsStore.Intent) {
         super.executeIntent(intent)
     }
 }