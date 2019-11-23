package com.arttttt.rotationcontrolv3.device.services.rotation.di

import com.arttttt.rotationcontrolv3.device.services.delegate.IWindowDelegate
import com.arttttt.rotationcontrolv3.device.services.delegate.WindowDelegate
import org.koin.dsl.module

val rotationServiceModule = module {
    factory<IWindowDelegate> {
        WindowDelegate(
            viewProvider = get(),
            windowManager = get()
        )
    }
}