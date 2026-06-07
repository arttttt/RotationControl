package com.arttttt.rotationcontrolv3

import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.arttttt.rotationcontrolv3.di.components.AppComponent
import com.arttttt.rotationcontrolv3.di.components.DaggerAppComponent
import com.arttttt.rotationcontrolv3.utils.di.AppComponentOwner
import timber.log.Timber

class App : MultiDexApplication(), AppComponentOwner {

    override val component: AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(
                context = this
            )
    }

    override fun onCreate() {
        super.onCreate()

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        Timber.plant(Timber.DebugTree())
    }
}