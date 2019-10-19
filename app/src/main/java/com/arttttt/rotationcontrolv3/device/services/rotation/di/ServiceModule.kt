package com.arttttt.rotationcontrolv3.device.services.rotation.di

import com.arttttt.rotationcontrolv3.device.services.delegate.IWindowDelegate
import com.arttttt.rotationcontrolv3.device.services.delegate.WindowDelegate
import com.arttttt.rotationcontrolv3.device.services.rotation.AccelerometerObserver
import org.koin.dsl.module

val rotationServiceModule = module {
    factory { AccelerometerObserver() }

    factory<IWindowDelegate> {
        WindowDelegate(
            context = get()
        )
    }
}