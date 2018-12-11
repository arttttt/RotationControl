package com.arttttt.rotationcontrolv3.presenter.about

class AboutPresenter: AboutContract.Presenter {
    override var mView: AboutContract.View? = null

    override fun onSourcesClicked() {
        mView?.showSourceCode()
    }

    override fun onDonateClicked() {
        mView?.showDonationPage()
    }
}