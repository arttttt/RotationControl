package com.arttttt.rotationcontrolv3.domain.stores.apporientaton
 
 import com.arkivanov.mvikotlin.core.store.Store
 import com.arttttt.rotationcontrolv3.domain.entity.apps.AppOrientation

interface AppOrientationStore : Store<AppOrientationStore.Intent, AppOrientationStore.State, AppOrientationStore.Label> {
 
     data class State(
         val lastLaunchedAppPkg: String?
     )
 
     sealed class Action
 
     sealed class Intent {

         data class ChangeLaunchedApp(
             val pkg: String,
         ) : Intent()
     }
 
     sealed class Message {

         data class LaunchedAppChanged(
             val pkg: String,
         ) : Message()
     }
 
     sealed class Label {

         data class LaunchedAppChanged(
             val pkg: String,
             val appOrientation: AppOrientation,
         ) : Label()
     }
 }