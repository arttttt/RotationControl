package com.arttttt.rotationcontrolv3.presenter

import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences
import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences.Companion.Parameters
import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper

class SettingsPresenter(private val preferences: AppPreferences,
                        private val serviceHelper: ServiceHelper): SettingsContract.Presenter {

    override var mView: SettingsContract.View? = null

    override fun init() {
        mView?.run {
            val startOnBoot = preferences.getBool(Parameters.START_ON_BOOT)
            setSwitchViewChecked(startOnBoot)
            serviceHelper.isStarted().value?.let { setFabIcon(it) }
            if (preferences.getBool(Parameters.FIRST_BOOT, true)) {
                checkAndRequestPermissions()
                preferences.putBool(Parameters.FIRST_BOOT, false)
            }
        }

        serviceHelper.isStarted().observeForever {
            it?.let { value ->
                mView?.run {
                    setFabIcon(value)
                }
            }
        }
    }

    override fun onFabClicked() {
        mView?.run { startOrStopService(serviceHelper) }
    }

    override fun onStartOnBootStateChanged(checked: Boolean) {
        preferences.putBool(AppPreferences.Companion.Parameters.START_ON_BOOT, checked)
    }
}