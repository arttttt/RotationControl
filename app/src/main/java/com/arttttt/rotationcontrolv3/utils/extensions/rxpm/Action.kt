package com.arttttt.rotationcontrolv3.utils.extensions.rxpm

import me.dmdev.rxpm.Action

fun<T> Action<T>.accept(value: T) {
    consumer.accept(value)
}

fun Action<Unit>.accept() {
    consumer.accept(Unit)
}