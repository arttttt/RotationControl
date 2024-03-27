package com.arttttt.rotationcontrolv3.domain.stores.rotation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arttttt.rotationcontrolv3.domain.entity.exceptions.NoPermissionsException
import com.arttttt.rotationcontrolv3.domain.entity.rotation.OrientationMode
import com.arttttt.rotationcontrolv3.domain.entity.rotation.RotationStatus
import com.arttttt.rotationcontrolv3.domain.entity.settings.Setting
import com.arttttt.rotationcontrolv3.domain.managers.ForcedOrientationManager
import com.arttttt.rotationcontrolv3.domain.repository.OrientationRepository
import com.arttttt.rotationcontrolv3.domain.repository.SensorsRepository
import com.arttttt.rotationcontrolv3.domain.repository.SettingsRepository
import com.arttttt.rotationcontrolv3.ui.rotation.PermissionsVerifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RotationExecutor(
    private val sensorsRepository: SensorsRepository,
    private val permissionsVerifier: PermissionsVerifier,
    private val orientationRepository: OrientationRepository,
    private val forcedOrientationManager: ForcedOrientationManager,
    private val settingsRepository: SettingsRepository,
) : CoroutineExecutor<RotationStore.Intent, RotationStore.Action, RotationStore.State, RotationStore.Message, RotationStore.Label>() {

    override fun executeAction(action: RotationStore.Action) {
        when (action) {
            is RotationStore.Action.GetGlobalOrientation -> getGlobalOrientation()
        }
    }

    override fun executeIntent(intent: RotationStore.Intent) {
        when (intent) {
            is RotationStore.Intent.SetGlobalOrientationMode -> setGlobalOrientation(intent.orientationMode)
            is RotationStore.Intent.SetAppOrientationMode -> setAppOrientation(intent.orientationMode)
        }
    }

    override fun dispose() {
        super.dispose()

        forcedOrientationManager.clear()
    }

    private fun setGlobalOrientation(mode: OrientationMode) {
        scope.launch {
            kotlin
                .runCatching {
                    setOrientation2(
                        currentOrientationMode = state().globalOrientationMode,
                        newOrientationMode = mode,
                    )
                }
                .map { RotationStore.Message.GlobalOrientationReceived(mode) }
                .recover(RotationStore.Message::ErrorOccurred)
                .getOrNull()
                ?.let(::dispatch)
        }
    }

    private fun setAppOrientation(mode: OrientationMode?) {
        val newMode = mode ?: state().globalOrientationMode ?: OrientationMode.Portrait

        scope.launch {
            kotlin
                .runCatching {
                    setOrientation2(
                        currentOrientationMode = state().appOrientationMode ?: state().globalOrientationMode,
                        newOrientationMode = newMode,
                    )
                }
                .map { RotationStore.Message.AppOrientationReceived(newMode) }
                .recover(RotationStore.Message::ErrorOccurred)
                .getOrNull()
                ?.let(::dispatch)
        }
    }

    private suspend fun setOrientation2(
        currentOrientationMode: OrientationMode?,
        newOrientationMode: OrientationMode,
    ) {
        val granted = withContext(Dispatchers.IO) {
            areAllPermissionsGranted()
        }

        if (!granted) throw NoPermissionsException()

        when (newOrientationMode) {
            is OrientationMode.Auto -> setAutoRotation(
                mode = newOrientationMode,
                forced = isForceModeEnabled(),
            )
            else -> {
                if (currentOrientationMode == OrientationMode.Auto) {
                    sensorsRepository.disableRotation()
                }

                setOrientation(
                    forced = isForceModeEnabled(),
                    mode = newOrientationMode,
                )
            }
        }
    }

    private fun getGlobalOrientation() {
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
                .onSuccess {
                    val mode = OrientationMode.Portrait

                    setOrientation2(
                        currentOrientationMode = null,
                        newOrientationMode = mode,
                    )

                    dispatch(RotationStore.Message.GlobalOrientationReceived(mode))
                }
                .onFailure { e ->
                    dispatch(RotationStore.Message.ErrorOccurred(e))
                }
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

    private suspend fun areAllPermissionsGranted(): Boolean {
        return permissionsVerifier.areAllPermissionsGranted2()
    }

    private suspend fun isForceModeEnabled(): Boolean {
        return settingsRepository.getSetting(Setting.ForcedMode::class).value
    }

    private fun createCorrectUpdateMessage(
        status: RotationStatus,
    ): RotationStore.Message {
        val factory = when {
            state().appOrientationMode != null -> RotationStore.Message::AppOrientationReceived
            else -> RotationStore.Message::GlobalOrientationReceived
        }

        return factory.invoke(
            when (status) {
                RotationStatus.Enabled -> OrientationMode.Auto
                RotationStatus.Disabled -> orientationRepository.getSystemOrientation()
            }
        )
    }

    private fun setAutoRotation(
        mode: OrientationMode,
        forced: Boolean,
    ) {
        if (forced) {
            forcedOrientationManager.forceApplyOrientation(
                mode = mode
            )
        } else {
            sensorsRepository.enableRotation()
        }
    }
}