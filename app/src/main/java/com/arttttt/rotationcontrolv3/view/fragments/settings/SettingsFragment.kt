package com.arttttt.rotationcontrolv3.view.fragments.settings

import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.presenter.settings.SettingsContract
import com.arttttt.rotationcontrolv3.view.fragments.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject

class SettingsFragment: BaseFragment<SettingsContract.View, SettingsContract.Presenter>(), SettingsContract.View {
    override fun getLayoutResource() = R.layout.fragment_settings
    override fun getMvpView() = this

    override val presenter: SettingsContract.Presenter by inject()

    override fun initializeUI() {
        startOnBootSwitch.setOnCheckedChangeListener { _, isChecked -> presenter.onStartOnBootStateChanged(isChecked) }
    }
}