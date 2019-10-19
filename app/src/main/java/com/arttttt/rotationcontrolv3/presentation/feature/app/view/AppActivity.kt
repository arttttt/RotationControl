package com.arttttt.rotationcontrolv3.presentation.feature.app.view

import android.content.Intent
import android.os.Bundle
import com.arttttt.rotationcontrolv3.R
import com.arttttt.rotationcontrolv3.presentation.feature.app.pm.AppPM
import com.arttttt.rotationcontrolv3.utils.APP_HOLDER
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.base.IResultHelper
import com.arttttt.rotationcontrolv3.utils.delegates.permissions.base.Result
import com.arttttt.rotationcontrolv3.utils.extensions.koilin.castTo
import com.arttttt.rotationcontrolv3.utils.extensions.koin.isDefinitionDeclared
import com.arttttt.rotationcontrolv3.utils.extensions.koin.koin
import com.arttttt.rotationcontrolv3.utils.extensions.rxpm.accept
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import me.dmdev.rxpm.base.PmActivity
import org.koin.android.ext.android.inject
import org.koin.android.scope.currentScope
import org.koin.core.qualifier.named
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.android.support.SupportAppNavigator

class AppActivity: PmActivity<AppPM>(), IResultHelper {

    private val resultSubject = PublishSubject.create<Result>()

    private val navigator = SupportAppNavigator(this, R.id.container)
    private val navigationHolder: NavigatorHolder by inject(named(APP_HOLDER))

    override fun onBindPresentationModel(pm: AppPM) {}

    override fun providePresentationModel(): AppPM {
        return currentScope.get()
    }

    override fun onBackPressed() {
        presentationModel.backPressed.accept()
    }

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