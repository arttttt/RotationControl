package com.arttttt.rotationcontrolv3.utils.rxjava

import io.reactivex.Scheduler

interface ISchedulersProvider {
    val ui: Scheduler
    val computation: Scheduler
    val trampoline: Scheduler
    val newThread: Scheduler
    val io: Scheduler
}