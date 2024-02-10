package com.arttttt.permissions.utils.extensions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.arttttt.permissions.domain.entity.IntentPermission
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.domain.entity.StandardPermission

fun Permission.Status.Companion.of(value: Int): Permission.Status {
    return if (value == PackageManager.PERMISSION_GRANTED) {
        Permission.Status.Granted
    } else {
        Permission.Status.Denied
    }
}

fun Permission.Status.Companion.of(value: Boolean): Permission.Status {
    return if (value) {
        Permission.Status.Granted
    } else {
        Permission.Status.Denied
    }
}

context(StandardPermission)
fun checkStatusImpl(context: Context): Permission.Status {
    return Permission.Status.of(ContextCompat.checkSelfPermission(context, permission))
}

fun checkStatusImpl(
    context: Context,
    permission: String,
): Permission.Status {
    return Permission.Status.of(ContextCompat.checkSelfPermission(context, permission))
}

fun Permission.Status.toBoolean(): Boolean {
    return when (this) {
        Permission.Status.Granted -> true
        Permission.Status.Denied -> false
    }
}