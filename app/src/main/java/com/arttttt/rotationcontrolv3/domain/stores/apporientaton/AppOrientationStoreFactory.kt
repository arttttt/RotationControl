package com.arttttt.rotationcontrolv3.domain.stores.apporientaton
 
 import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
 import com.arkivanov.mvikotlin.core.store.Store
 import com.arkivanov.mvikotlin.core.store.StoreFactory
 import com.arttttt.rotationcontrolv3.domain.repository.AppsRepository
 import javax.inject.Inject

class AppOrientationStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val appsRepository: AppsRepository,
 ) {
 
     fun create(): AppOrientationStore {
         val name = AppOrientationStore::class.qualifiedName
         val initialState = AppOrientationStore.State(
             lastLaunchedAppPkg = null,
         )
 
         return object : AppOrientationStore,
             Store<AppOrientationStore.Intent, AppOrientationStore.State, AppOrientationStore.Label> by storeFactory.create(
                 name = name,
                 initialState = initialState,
                 bootstrapper = SimpleBootstrapper(),
                 executorFactory = {
                     AppOrientationStoreExecutor(
                         appsRepository = appsRepository,
                     )
                 },
                 reducer = AppOrientationStoreReducer,
             ) {}
     }
 }