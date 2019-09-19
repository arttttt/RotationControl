package com.arttttt.rotationcontrolv3.presentation.feature.settings.presenter

import com.arttttt.rotationcontrolv3.utils.START_ON_BOOT
import com.arttttt.rotationcontrolv3.utils.delegates.preferences.PreferencesDelegate

class SettingsPresenter(
    private val preferences: PreferencesDelegate
): SettingsContract.Presenter {
    override var mView: SettingsContract.View? = null

    override fun onInitialization() {
        mView?.setStartOnBootState(preferences.getBool(START_ON_BOOT))
    }

    override fun onStartOnBootStateChanged(checked: Boolean) = preferences.putBool(START_ON_BOOT, checked)
}