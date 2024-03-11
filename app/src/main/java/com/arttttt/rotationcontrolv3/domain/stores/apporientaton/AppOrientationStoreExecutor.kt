package com.arttttt.rotationcontrolv3.domain.stores.apporientaton
 
 import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
 import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository

/**
  * todo: provide dispatchers provider
  */
 class AppOrientationStoreExecutor(
     private val appsRepository: AppsRepository,
 ) : CoroutineExecutor<AppOrientationStore.Intent, AppOrientationStore.Action, AppOrientationStore.State, AppOrientationStore.Message, AppOrientationStore.Label>() {
 
     override fun executeAction(action: AppOrientationStore.Action) {
         super.executeAction(action)
     }
 
     override fun executeIntent(intent: AppOrientationStore.Intent) {
         super.executeIntent(intent)
     }
 }