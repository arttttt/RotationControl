package com.arttttt.rotationcontrolv3.domain.stores.rotation

import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arttttt.rotationcontrolv3.domain.managers.ForcedOrientationManager
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.ui.rotation.PermissionsVerifier
import javax.inject.Inject

class RotationStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val sensorsRepository: SensorsRepository,
    private val permissionsVerifier: PermissionsVerifier,
    private val orientationRepository: OrientationRepository,
    private val forcedOrientationManager: ForcedOrientationManager,
    private val settingsRepository: SettingsRepository,
) {

    fun create(): RotationStore {
        return object : RotationStore,
            Store<RotationStore.Intent, RotationStore.State, RotationStore.Label> by storeFactory.create(
                name = RotationStore::class.java.name,
                initialState = RotationStore.State(
                    appOrientationMode = null,
                    globalOrientationMode = null,
                    error = null,
                ),
                bootstrapper = SimpleBootstrapper(
                    RotationStore.Action.GetGlobalOrientation,
                ),
                executorFactory = {
                    RotationExecutor(
                        sensorsRepository = sensorsRepository,
                        permissionsVerifier = permissionsVerifier,
                        orientationRepository = orientationRepository,
                        forcedOrientationManager = forcedOrientationManager,
                        settingsRepository = settingsRepository,
                    )
                },
                reducer = RotationReducer,
            ) {}
    }
}