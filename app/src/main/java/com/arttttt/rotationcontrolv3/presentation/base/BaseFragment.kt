package com.arttttt.rotationcontrolv3.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.transition.Fade
import me.dmdev.rxpm.base.PmFragment

abstract class BaseFragment<PM: BasePresentationModel>: PmFragment<PM>() {

    @get:LayoutRes
    abstract val layoutRes: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun getEnterTransition(): Any? {
        return Fade()
    }

    override fun getExitTransition(): Any? {
        return Fade()
    }

    final override fun onBindPresentationModel(pm: PM) {
        bindActions(pm)
        bindCommands(pm)
        bindStates(pm)
        bindRestActions(pm)
    }

    open fun bindActions(pm: PM) {}
    open fun bindCommands(pm: PM) {}
    open fun bindStates(pm: PM) {}
    open fun bindRestActions(pm: PM) {}
}