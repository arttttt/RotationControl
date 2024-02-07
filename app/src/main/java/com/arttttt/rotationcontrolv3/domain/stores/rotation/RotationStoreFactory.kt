package com.arttttt.rotationcontrolv3.domain.stores.rotation

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.ui.rotation.PermissionsVerifier
import javax.inject.Inject

class RotationStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val sensorsRepository: SensorsRepository,
    private val permissionsVerifier: PermissionsVerifier,
    private val orientationRepository: OrientationRepository,
) {

    fun create(): RotationStore {
        return object : RotationStore,
            Store<RotationStore.Intent, RotationStore.State, RotationStore.Label> by storeFactory.create(
                name = RotationStore::class.java.name,
                initialState = RotationStore.State(
                    orientationMode = null,
                    error = null,
                ),
                bootstrapper = SimpleBootstrapper(
                    RotationStore.Action.GetOrientation,
                    RotationStore.Action.SubscribeForAccelerometer,
                ),
                executorFactory = {
                    RotationExecutor(
                        sensorsRepository = sensorsRepository,
                        permissionsVerifier = permissionsVerifier,
                        orientationRepository = orientationRepository,
                    )
                },
                reducer = RotationReducer,
            ) {}
    }
}