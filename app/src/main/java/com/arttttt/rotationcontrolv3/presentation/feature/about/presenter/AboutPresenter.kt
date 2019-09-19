package com.arttttt.rotationcontrolv3.presentation.feature.about.presenter

class AboutPresenter:
    AboutContract.Presenter {
    override var mView: AboutContract.View? = null

    override fun onInitialization() {
        mView?.setVersion()
    }

    override fun onSourcesClicked() {
        mView?.showSourceCode()
    }

    override fun onDonateClicked() {
        mView?.showDonationPage()
    }
}