@file:Suppress("NOTHING_TO_INLINE")

package com.arttttt.rotationcontrolv3.presentation.base

import androidx.annotation.CallSuper
import com.arttttt.rotationcontrolv3.utils.APP_ROUTER
import com.arttttt.rotationcontrolv3.utils.delegates.errordispatcher.IErrorDispatcher
import com.arttttt.rotationcontrolv3.utils.rxjava.ISchedulersProvider
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import me.dmdev.rxpm.Command
import me.dmdev.rxpm.PresentationModel
import me.dmdev.rxpm.State
import me.dmdev.rxpm.action
import me.dmdev.rxpm.widget.DialogControl
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named
import ru.terrakok.cicerone.Router

abstract class BasePresentationModel: PresentationModel(), KoinComponent {

    protected open val router: Router by inject(named(APP_ROUTER))
    protected val errorDispatcher: IErrorDispatcher by inject()
    protected val schedulers: ISchedulersProvider by inject()

    val backPressed = action<Unit>()

    @CallSuper
    override fun onBind() {
        super.onBind()
    }

    @CallSuper
    override fun onCreate() {
        super.onCreate()

        backPressed
            .observable
            .subscribeUntilDestroy { backPressed() }
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
    }

    @CallSuper
    override fun onUnbind() {
        super.onUnbind()
    }

    protected abstract fun backPressed()

    protected open fun Completable.subscribeUntilDestroy(
        onError: ((Throwable) -> Unit) = errorDispatcher::dispatchError,
        onComplete: (() -> Unit) = {}
    ) {
        subscribe(onComplete, onError).untilDestroy()
    }

    protected open fun<T> Maybe<T>.subscribeUntilDestroy(
        onError: ((Throwable) -> Unit) = errorDispatcher::dispatchError,
        onComplete: (() -> Unit) = {},
        onSuccess: ((T) -> Unit) = {}
    ) {
        subscribe(onSuccess, onError, onComplete).untilDestroy()
    }

    protected open fun<T> Single<T>.subscribeUntilDestroy(
        onError: ((Throwable) -> Unit) = errorDispatcher::dispatchError,
        onSuccess: ((T) -> Unit) = {}
    ) {
        subscribe(onSuccess, onError).untilDestroy()
    }

    protected open fun<T> Observable<T>.subscribeUntilDestroy(
        onError: ((Throwable) -> Unit) = errorDispatcher::dispatchError,
        onComplete: (() -> Unit) = {},
        onNext: ((T) -> Unit) = {}
    ) {
        subscribe(onNext, onError, onComplete).untilDestroy()
    }

    protected inline fun<T> Command<T>.accept(value: T) {
        consumer.accept(value)
    }

    protected inline fun<T> State<T>.accept(value: T) {
        consumer.accept(value)
    }

    protected inline fun<R>DialogControl<Unit, R>.showForResult(): Maybe<R> {
        return showForResult(Unit)
    }

    protected infix fun <T> T.passTo(command: Command<T>) {
        command.consumer.accept(this)
    }

    protected infix fun <T> T.passTo(state: State<T>) {
        state.consumer.accept(this)
    }
}