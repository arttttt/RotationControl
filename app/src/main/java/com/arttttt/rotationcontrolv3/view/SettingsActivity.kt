package com.arttttt.rotationcontrolv3.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.model.permissions.AppPermissions
import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.presenter.SettingsContract
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings_content.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SettingsActivity : AppCompatActivity(), SettingsContract.View {

    private val permissions: AppPermissions by inject()
    private val presenter: SettingsContract.Presenter by inject { parametersOf(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setSupportActionBar(toolbar)

        presenter.attachView(this)
        presenter.init()

        fab.setOnClickListener { presenter.onFabClicked() }
        startOnBootSwitch.setOnCheckedChangeListener { _, isChecked -> presenter.onStartOnBootStateChanged(isChecked) }
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.detachView()
    }

    override fun checkAndRequestPermissions() {
        if (!permissions.canWriteSettings(this))
            permissions.requestWriteSettingsPermission(this)
    }

    override fun setFabIcon(started: Boolean) {
        val resourceId = if (started) R.drawable.ic_stop else R.drawable.ic_start
        fab.setImageResource(resourceId)
    }

    override fun setSwitchViewChecked(checked: Boolean) {
        startOnBootSwitch.isChecked = checked
    }

    override fun startOrStopService(serviceHelper: ServiceHelper) {
        serviceHelper.isStarted().value?.let {
            if (it)
                serviceHelper.stopService(applicationContext)
            else {
                if (!permissions.canWriteSettings(this)) {
                    permissions.requestWriteSettingsPermission(this)

                    return
                }

                serviceHelper.startService(applicationContext)
            }
        }
    }
}