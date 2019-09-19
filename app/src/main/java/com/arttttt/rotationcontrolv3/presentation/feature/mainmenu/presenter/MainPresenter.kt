package com.arttttt.rotationcontrolv3.presentation.feature.mainmenu.presenter

import com.arttttt.rotationcontrolv3.utils.delegates.preferences.PreferencesDelegate
import com.arttttt.rotationcontrolv3.device.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.utils.FIRST_BOOT

class MainPresenter(
    private val preferences: PreferencesDelegate,
    private val serviceHelper: ServiceHelper
): MainContract.Presenter {
    override var mView: MainContract.View? = null

    override fun initialize() {
        mView?.let { view ->
            view.onInitialize()

            if (preferences.getBool(FIRST_BOOT)) {
                view.checkAndRequestPermissions()
                preferences.putBool(FIRST_BOOT, false)
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