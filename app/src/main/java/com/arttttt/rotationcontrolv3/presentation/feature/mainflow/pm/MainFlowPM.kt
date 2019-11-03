package com.arttttt.rotationcontrolv3.presentation.feature.mainflow.pm

import android.view.MenuItem
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.Screens
import com.arttttt.rotationcontrolv3.device.services.rotation.helper.IRotationServiceHelper
import com.arttttt.rotationcontrolv3.presentation.base.BaseFlowPresentationModel
import com.arttttt.rotationcontrolv3.presentation.model.DialogResult
import com.arttttt.rotationcontrolv3.utils.FORCE_MODE
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.drawoverlays.ICanDrawOverlayChecker
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.drawoverlays.ICanDrawOverlayRequester
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings.ICanWriteSettingsChecker
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.writesystemsettings.ICanWriteSettingsRequester
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.IPreferencesDelegate
import io.reactivex.Single
import me.dmdev.rxpm.action
import me.dmdev.rxpm.state
import me.dmdev.rxpm.widget.dialogControl
import ru.terrakok.cicerone.android.support.SupportAppScreen

class MainFlowPM(
    private val preferencesDelegate: IPreferencesDelegate,
    private val rotationServiceHelper: IRotationServiceHelper,
    private val canWriteSettingsChecker: ICanWriteSettingsChecker,
    private val canWriteSettingsRequester: ICanWriteSettingsRequester,
    private val canDrawOverlayChecker: ICanDrawOverlayChecker,
    private val canDrawOverlayRequester: ICanDrawOverlayRequester
): BaseFlowPresentationModel() {

    val hamburgerClicked = action<Unit>()
    val navigationMenuClicked = action<MenuItem>()
    val fabClicked = action<Unit>()

    val fabIconRes = state(0)
    val fabVisibility = state(true)
    val currentScreen = state<SupportAppScreen>(Screens.SettingsScreen)

    val drawOverlayDialog = dialogControl<Unit, DialogResult>()
    val writeSettingsDialog = dialogControl<Unit, DialogResult>()
    val navigationDialog = dialogControl<Unit, Unit>()

    private lateinit var lastServiceStatus: IRotationServiceHelper.Status

    override fun backPressed() {
        router.exit()
    }

    override fun onCreate() {
        super.onCreate()

        fabClicked
            .observable
            .subscribeUntilDestroy { dispatchFabClick() }

        rotationServiceHelper
            .getStatusObservable()
            .subscribeUntilDestroy { status -> dispatchServiceStatus(status) }

        hamburgerClicked
            .observable
            .subscribeUntilDestroy { navigationDialog.show(Unit) }

        navigationMenuClicked
            .observable
            .subscribeUntilDestroy { item -> dispatchNavigationMenuClicked(item) }
    }

    private fun dispatchFabClick() {
        canWriteSettingsChecker
            .canWriteSettings()
            .flatMap { canWriteSystemSettings -> dispatchCanWriteSettings(canWriteSystemSettings) }
            .filter { canWriteSystemSettings -> canWriteSystemSettings }
            .map { preferencesDelegate.getBool(FORCE_MODE) }
            .flatMapSingle { isForceModeEnabled -> dispatchForceMode(isForceModeEnabled) }
            .filter { canDrawOverlay -> canDrawOverlay }
            .subscribeUntilDestroy {
                when (lastServiceStatus) {
                    IRotationServiceHelper.Status.STOPPED -> rotationServiceHelper.startRotationService()
                    IRotationServiceHelper.Status.STARTED -> rotationServiceHelper.stopRotationService()
                }
            }
    }

    private fun dispatchForceMode(isForceModeEnabled: Boolean): Single<Boolean> {
        return if (isForceModeEnabled) {
            canDrawOverlayChecker
                .canDrawOverlay()
                .flatMap { canDrawOverlay ->
                    if(!canDrawOverlay) {
                        drawOverlayDialog
                            .showForResult()
                            .filter { result -> result == DialogResult.OK }
                            .flatMapSingleElement { canDrawOverlayRequester.requestDrawOverlayPermission() }
                            .filter { canDrawOverlay -> canDrawOverlay }
                            .toSingle(false)
                    } else {
                        Single.just(true)
                    }
                }
        } else {
            Single.just(true)
        }
    }

    private fun dispatchCanWriteSettings(canWriteSystemSettings: Boolean): Single<Boolean> {
        return if (!canWriteSystemSettings) {
            writeSettingsDialog
                .showForResult()
                .filter { result -> result == DialogResult.OK }
                .flatMapSingleElement { canWriteSettingsRequester.requestWriteSettingsPermission() }
                .toSingle(false)
        } else {
            Single.just(canWriteSystemSettings)
        }
    }

    private fun dispatchServiceStatus(status: IRotationServiceHelper.Status) {
        lastServiceStatus = status

        when (status) {
            IRotationServiceHelper.Status.STARTED -> fabIconRes.accept(R.drawable.ic_stop)
            IRotationServiceHelper.Status.STOPPED -> fabIconRes.accept(R.drawable.ic_start)
        }
    }

    private fun dispatchNavigationMenuClicked(item: MenuItem) {
        fabVisibility.accept(item.itemId == R.id.settings_fragment_item)

        when (item.itemId) {
            R.id.settings_fragment_item -> Screens.SettingsScreen
            R.id.about_fragment_item -> Screens.AboutScreen
            else -> object : SupportAppScreen() {}
        } passTo currentScreen
    }
}