package com.arttttt.rotationcontrolv3.domain.stores.apps
 
 import com.arkivanov.mvikotlin.core.store.Store
 
 interface AppsStore : Store<AppsStore.Intent, AppsStore.State, AppsStore.Label> {
 
     data class State(
         val value: Int = 0
     )
 
     sealed class Action
 
     sealed class Intent
 
     sealed class Message
 
     sealed class Label
 }