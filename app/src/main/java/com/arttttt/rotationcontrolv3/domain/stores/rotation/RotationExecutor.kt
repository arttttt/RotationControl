package com.arttttt.rotationcontrolv3.domain.stores.rotation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.rotationcontrolv3.domain.entity.NoPermissionsException
import com.arttttt.rotationcontrolv3.domain.entity.OrientationMode
import com.arttttt.rotationcontrolv3.domain.entity.RotationStatus
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.ui.rotation.PermissionsVerifier
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RotationExecutor(
    private val sensorsRepository: SensorsRepository,
    private val permissionsVerifier: PermissionsVerifier,
    private val orientationRepository: OrientationRepository,
) : CoroutineExecutor<RotationStore.Intent, RotationStore.Action, RotationStore.State, RotationStore.Message, RotationStore.Label>() {

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

    private fun subscribeForAccelerometerEvents() {
        sensorsRepository
            .getRotationStatuses()
            .filter { permissionsVerifier.areAllPermissionsGranted() }
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
        scope.launch {
            kotlin
                .runCatching {
                    if (!permissionsVerifier.areAllPermissionsGranted()) {
                        throw NoPermissionsException()
                    }
                }
                .map {
                    when (mode) {
                        OrientationMode.Auto -> sensorsRepository.enableRotation()
                        else -> {
                            sensorsRepository.disableRotation()
                            orientationRepository.setOrientation(mode)
                        }
                    }

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
                    if (!permissionsVerifier.areAllPermissionsGranted()) {
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
}