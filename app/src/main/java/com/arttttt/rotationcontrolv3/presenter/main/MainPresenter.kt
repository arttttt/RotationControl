package com.arttttt.rotationcontrolv3.presenter.main

import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences
import com.arttttt.rotationcontrolv3.model.preferences.AppPreferences.Companion.Parameters
import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper

class MainPresenter(private val preferences: AppPreferences,
                    private val serviceHelper: ServiceHelper
): MainContract.Presenter {
    override var mView: MainContract.View? = null

    override fun initialize() {
        mView?.let { view ->
            view.onInitialize()

            if (preferences.getBool(Parameters.FIRST_BOOT, true)) {
                view.checkAndRequestPermissions()
                preferences.putBool(Parameters.FIRST_BOOT, false)
            }

            serviceHelper.isStarted().observeForever { value ->
                value?.let(view::setFabIcon)
            }
        }
    }

    override fun onFabClicked() {
        mView?.startOrStopService(serviceHelper)
    }
}