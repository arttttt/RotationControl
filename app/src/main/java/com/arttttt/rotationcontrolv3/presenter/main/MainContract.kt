package com.arttttt.rotationcontrolv3.presenter.main

import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.presenter.base.MvpPresenter
import com.arttttt.rotationcontrolv3.presenter.base.MvpView

interface MainContract {
    interface Presenter: MvpPresenter<View> {
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