package com.arttttt.rotationcontrolv3.presentation.feature.about.presenter

import com.arttttt.rotationcontrolv3.presentation.base.MvpPresenter
import com.arttttt.rotationcontrolv3.presentation.base.MvpView

interface AboutContract {
    interface Presenter:
        MvpPresenter<View> {
        fun onInitialization()
        fun onSourcesClicked()
        fun onDonateClicked()
    }
    interface View: MvpView {
        fun setVersion()
        fun showSourceCode()
        fun showDonationPage()
    }
}