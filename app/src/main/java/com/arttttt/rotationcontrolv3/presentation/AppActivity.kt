package com.arttttt.rotationcontrolv3.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.Screens
import com.arttttt.rotationcontrolv3.utils.APP_HOLDER
import com.arttttt.rotationcontrolv3.utils.APP_ROUTER
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.helper.ActivityHolder
import io.reactivex.functions.Consumer
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class AppActivity: AppCompatActivity() {

    private val navigator = SupportAppNavigator(this, R.id.container)
    private val navigationHolder: NavigatorHolder by inject(named(APP_HOLDER))

    private val router: Router by inject(named(APP_ROUTER))

    private val activityHolder: ActivityHolder by inject()
    private val resultConsumer: Consumer<Int> by inject()

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
        activityHolder.detachActivity()
    }

    override fun onResume() {
        navigationHolder.setNavigator(navigator)
        activityHolder.attachActivity(this)
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.newRootScreen(Screens.MainFlowScreen)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        resultConsumer.accept(requestCode)
    }
}