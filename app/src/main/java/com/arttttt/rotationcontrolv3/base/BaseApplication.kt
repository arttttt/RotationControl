package com.arttttt.rotationcontrolv3.base

import android.app.Application
import com.arttttt.rotationcontrolv3.di.AppModule
import org.koin.android.ext.android.startKoin

class BaseApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(AppModule.module))
    }
}