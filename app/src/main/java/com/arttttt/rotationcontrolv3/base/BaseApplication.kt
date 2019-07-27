package com.arttttt.rotationcontrolv3.base

import android.app.Application
import com.arttttt.rotationcontrolv3.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BaseApplication)
            modules(AppModule.module)
        }
    }
}