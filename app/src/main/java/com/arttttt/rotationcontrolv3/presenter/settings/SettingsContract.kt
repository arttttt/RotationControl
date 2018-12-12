package com.arttttt.rotationcontrolv3.presenter.settings

import com.arttttt.rotationcontrolv3.presenter.base.MvpPresenter
import com.arttttt.rotationcontrolv3.presenter.base.MvpView

interface SettingsContract {
    interface Presenter: MvpPresenter<View> {
        fun onInitialization()
        fun onStartOnBootStateChanged(checked: Boolean)
    }

    interface View: MvpView {
        fun setStartOnBootState(checked: Boolean)
    }
}