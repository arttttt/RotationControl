package com.arttttt.rotationcontrolv3.presenter.about

import com.arttttt.rotationcontrolv3.presenter.base.MvpPresenter
import com.arttttt.rotationcontrolv3.presenter.base.MvpView

interface AboutContract {
    interface Presenter: MvpPresenter<View> {
        fun onSourcesClicked()
        fun onDonateClicked()
    }
    interface View: MvpView {
        fun showSourceCode()
        fun showDonationPage()
    }
}