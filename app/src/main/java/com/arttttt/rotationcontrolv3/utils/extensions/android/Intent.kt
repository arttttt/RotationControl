@file:Suppress("NOTHING_TO_INLINE")

package com.arttttt.rotationcontrolv3.utils.extensions.android

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.arttttt.rotationcontrolv3.utils.delegates.bundle.BundleExtractorDelegate
import kotlin.properties.ReadOnlyProperty

inline fun intentOf(action: String, block: Intent.() -> Unit = {}): Intent {
    return Intent(action).apply(block)
}

inline fun intentOf(action: String, uri: Uri, block: Intent.() -> Unit = {}): Intent {
    return Intent(action, uri).apply(block)
}

inline fun<reified T: Any> intentOf(context: Context, block: Intent.() -> Unit = {}): Intent {
    return Intent(context, T::class.java).apply(block)
}

inline fun Context.startForegroundServiceCompat(intent: Intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(intent)
    } else {
        startService(intent)
    }
}

inline fun<reified T:Any> stopService(context: Context) {
    context.stopService(intentOf<T>(context))
}

inline fun <reified T> Intent.extra(
    key: String,
    defaultValue: T? = null
): ReadOnlyProperty<Any?, T> =
    BundleExtractorDelegate {
        BundleExtractorDelegate.extractFromBundle(
            bundle = extras,
            key = key,
            defaultValue = defaultValue
        )
    }
