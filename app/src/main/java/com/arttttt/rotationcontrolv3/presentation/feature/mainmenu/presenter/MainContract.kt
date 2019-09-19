package com.arttttt.rotationcontrolv3.presentation.feature.mainmenu.presenter

import com.arttttt.rotationcontrolv3.device.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.presentation.base.MvpPresenter
import com.arttttt.rotationcontrolv3.presentation.base.MvpView

interface MainContract {
    interface Presenter:
        MvpPresenter<View> {
        fun initialize()
        fun onFabClicked()
    }

    interface View: MvpView {
        fun checkAndRequestPermissions()
        fun onInitialize()
        fun setFabIcon(started: Boolean)
        fun startOrStopService(serviceHelper: ServiceHelper)
    }
}