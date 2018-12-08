package com.arttttt.rotationcontrolv3.presenter

import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.presenter.base.MvpPresenter
import com.arttttt.rotationcontrolv3.presenter.base.MvpView

interface SettingsContract {
    interface Presenter: MvpPresenter<View> {
        fun init()
        fun onFabClicked()
        fun onStartOnBootStateChanged(checked: Boolean)
    }
    interface View: MvpView {
        fun checkAndRequestPermissions()
        fun setFabIcon(started: Boolean)
        fun setSwitchViewChecked(checked: Boolean)
        fun startOrStopService(serviceHelper: ServiceHelper)
    }
}