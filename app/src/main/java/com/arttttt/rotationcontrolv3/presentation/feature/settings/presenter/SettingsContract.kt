package com.arttttt.rotationcontrolv3.presentation.feature.settings.presenter

import com.arttttt.rotationcontrolv3.presentation.base.MvpPresenter
import com.arttttt.rotationcontrolv3.presentation.base.MvpView

interface SettingsContract {
    interface Presenter:
        MvpPresenter<View> {
        fun onInitialization()
        fun onStartOnBootStateChanged(checked: Boolean)
    }

    interface View: MvpView {
        fun setStartOnBootState(checked: Boolean)
    }
}