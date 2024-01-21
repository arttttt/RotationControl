package com.arttttt.rotationcontrolv3

import androidx.multidex.MultiDexApplication
import com.arttttt.rotationcontrolv3.di.AppComponent
import com.arttttt.rotationcontrolv3.di.DaggerAppComponent
import com.arttttt.rotationcontrolv3.utils.di.AppComponentOwner

class App : MultiDexApplication(), AppComponentOwner {

    override val component: AppComponent by lazy {
        DaggerAppComponent
            .factory()
            .create(
                context = this
            )
    }
}