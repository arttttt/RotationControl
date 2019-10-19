package com.arttttt.rotationcontrolv3.presentation.feature.settings.pm

import com.arttttt.rotationcontrolv3.presentation.base.BaseFlowPresentationModel
import com.arttttt.rotationcontrolv3.utils.FORCE_MODE
import com.arttttt.rotationcontrolv3.utils.START_ON_BOOT
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.IPreferencesDelegate
import me.dmdev.rxpm.widget.checkControl

class SettingsPM(
    private val preferencesDelegate: IPreferencesDelegate
): BaseFlowPresentationModel() {

    val startOnBootControl = checkControl(preferencesDelegate.getBool(START_ON_BOOT))
    val forceModelControl = checkControl(preferencesDelegate.getBool(FORCE_MODE))

    override fun onCreate() {
        super.onCreate()

        startOnBootControl
            .checkedChanges
            .observable
            .subscribeUntilDestroy { isChecked -> preferencesDelegate.putBool(START_ON_BOOT, isChecked) }

        forceModelControl
            .checkedChanges
            .observable
            .subscribeUntilDestroy { isChecked -> preferencesDelegate.putBool(FORCE_MODE, isChecked) }
    }

    override fun backPressed() {
        router.exit()
    }
}