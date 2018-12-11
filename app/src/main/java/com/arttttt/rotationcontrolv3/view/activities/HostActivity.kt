package com.arttttt.rotationcontrolv3.view.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.model.permissions.AppPermissions
import com.arttttt.rotationcontrolv3.model.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.presenter.main.MainContract
import com.arttttt.rotationcontrolv3.view.fragments.mainmenu.MainMenuFragment
import kotlinx.android.synthetic.main.activity_host.*
import org.koin.android.ext.android.inject

class HostActivity : AppCompatActivity(), MainContract.View {

    private val permissions: AppPermissions by inject()
    private val presenter: MainContract.Presenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        setSupportActionBar(bottomToolbar)

        presenter.attachView(this)
        presenter.initialize()
    }

    override fun checkAndRequestPermissions() {
        if (!permissions.canWriteSettings(this))
            permissions.requestWriteSettingsPermission(this)
    }

    override fun onInitialize() {
        bottomToolbar.inflateMenu(R.menu.menu_main)
        bottomToolbar.setNavigationOnClickListener {
            MainMenuFragment()
                .apply {
                    arguments = Bundle().apply { putInt(MainMenuFragment.navHostIdKey,
                        R.id.navHostFragment
                    ) }
                }
                .show(supportFragmentManager, MainMenuFragment.tag)
        }

        fab.setOnClickListener { presenter.onFabClicked() }

        val navController = Navigation.findNavController(this, R.id.navHostFragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.settingsFragment -> fab.show()
                R.id.aboutFragment -> fab.hide()
            }
        }
    }

    override fun setFabIcon(started: Boolean) {
        val resourceId = if (started) R.drawable.ic_stop else R.drawable.ic_start
        fab.setImageResource(resourceId)
        fab.hide()
        fab.show()
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
