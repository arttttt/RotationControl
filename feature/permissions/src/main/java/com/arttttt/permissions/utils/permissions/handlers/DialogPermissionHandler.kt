package com.arttttt.permissions.utils.permissions.handlers

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.arttttt.permissions.domain.entity.IntentPermission
import com.arttttt.permissions.domain.entity.Permission2
import com.arttttt.permissions.utils.permissions.PermissionHandler
import kotlinx.coroutines.channels.Channel

abstract class DialogPermissionHandler<T : IntentPermission> : PermissionHandler<T> {

    /**
     * todo: move to a separated class
     */
    private val lifecycleObserver = object : DefaultLifecycleObserver {
        private var skipNextResume = false
        private var resumeChannel: Channel<Unit>? = null

        fun init(lifecycle: Lifecycle) {
            if (lifecycle.currentState == Lifecycle.State.RESUMED) {
                skipNextResume = true
            }

            resumeChannel = Channel(Channel.CONFLATED)
        }

        fun clear() {
            resumeChannel?.close()
            resumeChannel = null
        }

        override fun onResume(owner: LifecycleOwner) {
            if (skipNextResume) {
                skipNextResume = false
            } else {
                resumeChannel?.trySend(Unit)
            }
        }

        suspend fun awaitResume() {
            resumeChannel?.receive()
        }
    }

    protected abstract fun Context.checkPermissionStatus(): Permission2.Status

    override suspend fun requestPermission(
        activity: ComponentActivity,
        permission: T
    ): Permission2.Status {
        lifecycleObserver.init(activity.lifecycle)
        activity.lifecycle.addObserver(lifecycleObserver)

        val intent = permission.createIntent(activity)

        return try {
            if (intent.resolveActivity(activity.applicationContext.packageManager) != null) {
                activity.startActivity(intent)
                lifecycleObserver.awaitResume()
            }

            activity.applicationContext.checkPermissionStatus()
        } finally {
            lifecycleObserver.clear()
            activity.lifecycle.removeObserver(lifecycleObserver)
        }
    }
}
