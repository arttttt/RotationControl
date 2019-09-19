package com.arttttt.rotationcontrolv3.presentation.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class BaseFragment<V: MvpView,P: MvpPresenter<V>>: androidx.fragment.app.Fragment() {
    @LayoutRes
    protected abstract fun getLayoutResource(): Int

    protected abstract fun getMvpView(): V

    protected abstract val presenter: P

    protected abstract fun initializeUI()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutResource(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attachView(getMvpView())
        initializeUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()

        presenter.detachView()
    }
}