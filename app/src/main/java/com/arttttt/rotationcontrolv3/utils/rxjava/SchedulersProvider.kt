package com.arttttt.rotationcontrolv3.utils.rxjava

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SchedulersProvider: ISchedulersProvider {
    override val ui: Scheduler = AndroidSchedulers.mainThread()
    override val computation: Scheduler = Schedulers.computation()
    override val trampoline: Scheduler = Schedulers.trampoline()
    override val newThread: Scheduler = Schedulers.newThread()
    override val io: Scheduler = Schedulers.io()
}