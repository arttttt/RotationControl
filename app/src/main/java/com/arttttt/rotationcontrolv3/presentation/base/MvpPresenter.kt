package com.arttttt.rotationcontrolv3.presentation.base

interface MvpPresenter<T: MvpView> {
    var mView: T?
    fun attachView(view: T) {
        mView = view
    }
    fun detachView() {
        mView = null
    }
}