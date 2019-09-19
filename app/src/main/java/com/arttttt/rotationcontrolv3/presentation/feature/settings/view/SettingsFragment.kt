package com.arttttt.rotationcontrolv3.presentation.feature.settings.view

import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.presentation.feature.settings.presenter.SettingsContract
import com.arttttt.rotationcontrolv3.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.android.ext.android.inject
import org.koin.android.scope.currentScope

class SettingsFragment: BaseFragment<SettingsContract.View, SettingsContract.Presenter>(), SettingsContract.View {
    override fun getLayoutResource() = R.layout.fragment_settings
    override fun getMvpView() = this

    override val presenter: SettingsContract.Presenter by currentScope.inject()

    override fun initializeUI() {
        presenter.onInitialization()
        startOnBootSwitch.setOnCheckedChangeListener { _, isChecked -> presenter.onStartOnBootStateChanged(isChecked) }
    }

    override fun setStartOnBootState(checked: Boolean) {
        startOnBootSwitch.isChecked = checked
    }
}