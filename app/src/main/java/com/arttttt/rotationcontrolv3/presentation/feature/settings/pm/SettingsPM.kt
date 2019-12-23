package com.arttttt.rotationcontrolv3.presentation.feature.settings.pm

import com.arttttt.rotationcontrolv3.device.services.rotation.helper.IRotationServiceHelper
import com.arttttt.rotationcontrolv3.presentation.base.BaseFlowPresentationModel
import com.arttttt.rotationcontrolv3.presentation.model.DialogResult
import com.arttttt.rotationcontrolv3.utils.FORCE_MODE
import com.arttttt.rotationcontrolv3.utils.START_ON_BOOT
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.PermissionsManager
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.actions.Permissions
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.IPreferencesDelegate
import io.reactivex.Single
import me.dmdev.rxpm.widget.checkControl
import me.dmdev.rxpm.widget.dialogControl

class SettingsPM(
    private val preferencesDelegate: IPreferencesDelegate,
    private val rotationServiceHelper: IRotationServiceHelper,
    private val permissionsManager: PermissionsManager
): BaseFlowPresentationModel() {

    val startOnBootControl = checkControl(preferencesDelegate.getBool(START_ON_BOOT))
    val forceModelControl = checkControl(preferencesDelegate.getBool(FORCE_MODE))

    val drawOverlayDialog = dialogControl<Unit, DialogResult>()

    private lateinit var lastServiceStatus: IRotationServiceHelper.Status

    override fun onCreate() {
        super.onCreate()

        rotationServiceHelper
            .getStatusObservable()
            .subscribeUntilDestroy { status -> lastServiceStatus = status }

        startOnBootControl
            .checkedChanges
            .observable
            .subscribeUntilDestroy { isChecked -> preferencesDelegate.putBool(START_ON_BOOT, isChecked) }

        forceModelControl
            .checkedChanges
            .observable
            .flatMapSingle { isChecked ->
                if (isChecked && lastServiceStatus == IRotationServiceHelper.Status.STARTED) {
                    permissionsManager
                        .checkPermission(Permissions.DrawOverlays())
                        .flatMap { canDrawOverlay ->
                            if (!canDrawOverlay) {
                                drawOverlayDialog
                                    .showForResult()
                                    .filter { result -> result == DialogResult.OK }
                                    .flatMapSingleElement { permissionsManager.checkPermission(Permissions.DrawOverlays()) }
                                    .filter { canDrawOverlay -> canDrawOverlay }
                                    .toSingle(false)
                                    .doOnSuccess { canDrawOverlay ->
                                        if (!canDrawOverlay) {
                                            forceModelControl.checked.accept(false)
                                        } else {
                                            rotationServiceHelper.restartRotationService()
                                        }
                                    }
                            } else {
                                Single
                                    .just(isChecked)
                                    .doOnSuccess { rotationServiceHelper.restartRotationService() }
                            }
                        }
                } else {
                    Single.just(isChecked)
                }
            }
            .subscribeUntilDestroy { isChecked -> preferencesDelegate.putBool(FORCE_MODE, isChecked) }
    }

    override fun backPressed() {
        router.exit()
    }
}