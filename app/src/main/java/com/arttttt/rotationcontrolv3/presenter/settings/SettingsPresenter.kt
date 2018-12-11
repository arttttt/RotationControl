package com.arttttt.rotationcontrolv3.presenter.settings

import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences
import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences.Companion.Parameters

class SettingsPresenter(private val preferences: AppPreferences): SettingsContract.Presenter {
    override var mView: SettingsContract.View? = null

    override fun onStartOnBootStateChanged(checked: Boolean) = preferences.putBool(Parameters.START_ON_BOOT, checked)
}