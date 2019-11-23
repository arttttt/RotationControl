package com.arttttt.rotationcontrolv3.device.services.rotation.base

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.annotation.CallSuper
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.inject
import org.koin.core.module.Module

abstract class BaseService: Service() {
    companion object: KoinComponent {
        internal val destroyDisposable: CompositeDisposable by inject()

        internal fun<T> Single<T>.subscribeUntilDestroy(
            onError: ((Throwable) -> Unit) = {},
            onSuccess: ((T) -> Unit) = {}
        ) {
            subscribe(onSuccess, onError).let(destroyDisposable::add)
        }

        internal fun<T> Observable<T>.subscribeUntilDestroy(
            onError: ((Throwable) -> Unit) = {},
            onComplete: (() -> Unit) = {},
            onNext: ((T) -> Unit) = {}
        ) {
            subscribe(onNext, onError, onComplete).let(destroyDisposable::add)
        }

        internal fun<T> Maybe<T>.subscribeUntilDestroy(
            onError: ((Throwable) -> Unit) = {},
            onComplete: (() -> Unit) = {},
            onNext: ((T) -> Unit) = {}
        ) {
            subscribe(onNext, onError, onComplete).let(destroyDisposable::add)
        }

        internal fun Completable.subscribeUntilDestroy(
            onError: ((Throwable) -> Unit) = {},
            onComplete: (() -> Unit) = {}
        ) {
            subscribe(onComplete, onError).let(destroyDisposable::add)
        }
    }

    private lateinit var modules: List<Module>

    override fun onBind(intent: Intent?): IBinder? = null

    open fun provideKoinModules(): List<Module> {
        return emptyList()
    }

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        modules = provideKoinModules()
        loadKoinModules(modules)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        unloadKoinModules(modules)
        destroyDisposable.clear()
    }
}