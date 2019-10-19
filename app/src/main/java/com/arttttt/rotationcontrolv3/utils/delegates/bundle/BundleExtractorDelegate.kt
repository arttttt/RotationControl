package com.arttttt.rotationcontrolv3.utils.delegates.bundle

import android.os.Bundle
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class BundleExtractorDelegate<R, T>(private val initializer: (R) -> T) : ReadOnlyProperty<R, T> {

    companion object {
        inline fun <reified T> extractFromBundle(
            bundle: Bundle?,
            key: String,
            defaultValue: T? = null
        ): T {
            val result = bundle?.get(key) ?: defaultValue
            if (result != null && result !is T) {
                throw ClassCastException("Property $key has different class type")
            }
            return result as T
        }
    }

    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: R, property: KProperty<*>): T {
        if (value == EMPTY) {
            value = initializer(thisRef)
        }

        return value as T
    }
}
