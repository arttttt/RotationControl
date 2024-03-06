package com.arttttt.rotationcontrolv3.domain.stores.rotation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.rotationcontrolv3.domain.managers.ForcedOrientationManager
import com.arttttt.rotationcontrolv3.domain.entity.exceptions.NoPermissionsException
import com.arttttt.rotationcontrolv3.domain.entity.rotation.OrientationMode
import com.arttttt.rotationcontrolv3.domain.entity.rotation.RotationStatus
import com.arttttt.rotationcontrolv3.domain.entity.settings.Setting
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.ui.rotation.PermissionsVerifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RotationExecutor(
    private val sensorsRepository: SensorsRepository,
    private val permissionsVerifier: PermissionsVerifier,
    private val orientationRepository: OrientationRepository,
    private val forcedOrientationManager: ForcedOrientationManager,
    private val settingsRepository: SettingsRepository,
) : CoroutineExecutor<RotationStore.Intent, RotationStore.Action, RotationStore.State, RotationStore.Message, RotationStore.Label>() {

    private var accelerometerEventsJob: Job? = null

    override fun executeAction(action: RotationStore.Action) {
        when (action) {
            is RotationStore.Action.GetOrientation -> getOrientation()
            is RotationStore.Action.SubscribeForAccelerometer -> subscribeForAccelerometerEvents()
        }
    }

    override fun executeIntent(intent: RotationStore.Intent) {
        when (intent) {
            is RotationStore.Intent.SetOrientationMode -> setOrientation(intent.orientationMode)
        }
    }

    override fun dispose() {
        super.dispose()

        forcedOrientationManager.clear()
    }

    private fun subscribeForAccelerometerEvents() {
        accelerometerEventsJob = sensorsRepository
            .getRotationStatuses()
            .distinctUntilChanged()
            .filter {
                permissionsVerifier.areAllPermissionsGranted(
                    forced = withContext(Dispatchers.IO) {
                        settingsRepository.getSetting(Setting.ForcedMode::class).value
                    }
                )
            }
            .onEach { status ->
                dispatch(
                    RotationStore.Message.OrientationReceived(
                        orientationMode = when (status) {
                            RotationStatus.Enabled -> OrientationMode.Auto
                            RotationStatus.Disabled -> orientationRepository.getSystemOrientation()
                        }
                    ),
                )
            }
            .launchIn(scope)
    }

    private fun setOrientation(mode: OrientationMode) {
        accelerometerEventsJob?.cancel()

        scope.launch {
            kotlin
                .runCatching {
                    val forced = withContext(Dispatchers.IO) {
                        settingsRepository.getSetting(Setting.ForcedMode::class).value
                    }

                    if (!permissionsVerifier.areAllPermissionsGranted(forced)) {
                        throw NoPermissionsException()
                    }

                    forced
                }
                .map { forced ->
                    when (mode) {
                        OrientationMode.Auto -> sensorsRepository.enableRotation()
                        else -> {
                            if (state().orientationMode == OrientationMode.Auto) {
                                sensorsRepository.disableRotation()
                            }

                            setOrientation(
                                forced = forced,
                                mode = mode,
                            )
                        }
                    }

                    subscribeForAccelerometerEvents()

                    RotationStore.Message.OrientationReceived(mode)
                }
                .recover(RotationStore.Message::ErrorOccurred)
                .getOrThrow()
                .let(::dispatch)
        }
    }

    private fun getOrientation() {
        scope.launch {
            kotlin
                .runCatching {
                    val forced = withContext(Dispatchers.IO) {
                        settingsRepository.getSetting(Setting.ForcedMode::class).value
                    }

                    if (!permissionsVerifier.areAllPermissionsGranted(forced)) {
                        throw NoPermissionsException()
                    }
                }
                .map {
                    orientationRepository
                        .getSystemOrientation()
                        .let(RotationStore.Message::OrientationReceived)
                }
                .recover(RotationStore.Message::ErrorOccurred)
                .getOrThrow()
                .let(::dispatch)
        }
    }

    private fun setOrientation(
        forced: Boolean,
        mode: OrientationMode
    ) {
        if (forced) {
            forcedOrientationManager.forceApplyOrientation(mode)
        } else {
            orientationRepository.setOrientation(mode)
        }
    }
}