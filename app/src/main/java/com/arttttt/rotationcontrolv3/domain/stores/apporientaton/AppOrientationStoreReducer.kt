package com.arttttt.rotationcontrolv3.domain.stores.apporientaton
 
 import com.arkivanov.mvikotlin.core.store.Reducer
 
 object AppOrientationStoreReducer : Reducer<AppOrientationStore.State, AppOrientationStore.Message> {
 
     override fun AppOrientationStore.State.reduce(msg: AppOrientationStore.Message): AppOrientationStore.State {
         return when (msg) {
             is AppOrientationStore.Message.LaunchedAppChanged -> copy(
                 lastLaunchedAppPkg = msg.pkg,
             )
         }
     }
 }