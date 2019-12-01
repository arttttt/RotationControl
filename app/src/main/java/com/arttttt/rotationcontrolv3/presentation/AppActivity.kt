package com.arttttt.rotationcontrolv3.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.Screens
import com.arttttt.rotationcontrolv3.utils.APP_HOLDER
import com.arttttt.rotationcontrolv3.utils.APP_ROUTER
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.base.IPermissionResultHelper
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.base.Result
import com.arttttt.rotationcontrolv3.utils.extensions.koilin.castTo
import com.arttttt.rotationcontrolv3.utils.extensions.koin.isDefinitionDeclared
import com.arttttt.rotationcontrolv3.utils.extensions.koin.koin
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

    private val permissionResultHelper: IPermissionResultHelper by inject ()
    private val permissionResultConsumer: Consumer<Result> by inject()

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
        permissionResultHelper.detachAppActivity()
    }

    override fun onResume() {
        navigationHolder.setNavigator(navigator)
        permissionResultHelper.attachAppActivity(this)
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!koin.isDefinitionDeclared<IPermissionResultHelper>()) {
            koin.declare(this.castTo<IPermissionResultHelper>())
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.newRootScreen(Screens.MainFlowScreen)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        permissionResultConsumer.accept(
            Result(
                requestCode = requestCode,
                resultCode = resultCode,
                data = data
            )
        )
    }
}