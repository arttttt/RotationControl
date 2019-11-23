package com.arttttt.rotationcontrolv3.presentation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.Screens
import com.arttttt.rotationcontrolv3.utils.APP_HOLDER
import com.arttttt.rotationcontrolv3.utils.APP_ROUTER
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.base.IResultHelper
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.base.Result
import com.arttttt.rotationcontrolv3.utils.extensions.koilin.castTo
import com.arttttt.rotationcontrolv3.utils.extensions.koin.isDefinitionDeclared
import com.arttttt.rotationcontrolv3.utils.extensions.koin.koin
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class AppActivity: AppCompatActivity(), IResultHelper {

    private val resultSubject = PublishSubject.create<Result>()

    private val navigator = SupportAppNavigator(this, R.id.container)
    private val navigationHolder: NavigatorHolder by inject(named(APP_HOLDER))

    private val router: Router by inject(named(APP_ROUTER))

    override fun onPause() {
        super.onPause()
        navigationHolder.removeNavigator()
    }

    override fun onResume() {
        navigationHolder.setNavigator(navigator)
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!koin.isDefinitionDeclared<IResultHelper>()) {
            koin.declare(this.castTo<IResultHelper>())
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            router.newRootScreen(Screens.MainFlowScreen)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        resultSubject.onNext(
            Result(
                requestCode = requestCode,
                resultCode = resultCode,
                data = data
            )
        )
    }

    override fun onResultReceived(): Observable<Result> {
        return resultSubject.serialize()
    }

    override fun startForResult(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }
}