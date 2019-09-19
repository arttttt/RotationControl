package com.arttttt.rotationcontrolv3.presentation.feature

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.IPermissionsCheckerDelegate
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.IPermissionsRequesterDelegate
import com.arttttt.rotationcontrolv3.device.services.base.ServiceHelper
import com.arttttt.rotationcontrolv3.presentation.feature.mainmenu.presenter.MainContract
import com.arttttt.rotationcontrolv3.presentation.feature.mainmenu.view.MainMenuFragment
import kotlinx.android.synthetic.main.activity_host.*
import org.koin.android.ext.android.inject
import org.koin.android.scope.currentScope

class HostActivity : AppCompatActivity(), MainContract.View {

    private val permissionsChecker: IPermissionsCheckerDelegate by inject()
    private val permissionsRequester: IPermissionsRequesterDelegate by inject()
    private val presenter: MainContract.Presenter by currentScope.inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_host)
        setSupportActionBar(bottomToolbar)

        presenter.attachView(this)
        presenter.initialize()
    }

    override fun checkAndRequestPermissions() {
        if (!permissionsChecker.canWriteSettings())
            permissionsRequester.requestWriteSettingsPermission()
    }

    override fun onInitialize() {
        bottomToolbar.inflateMenu(R.menu.menu_main)
        bottomToolbar.setNavigationOnClickListener {
            MainMenuFragment()
                .apply {
                    arguments = Bundle().apply { putInt(
                        MainMenuFragment.navHostIdKey,
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
                if (!permissionsChecker.canWriteSettings()) {
                    permissionsRequester.requestWriteSettingsPermission()

                    return
                }

                serviceHelper.startService(applicationContext)
            }
        }
    }
}
