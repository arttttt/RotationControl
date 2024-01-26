package com.arttttt.permissions.domain.entity

import android.content.Context
import com.arttttt.permissions.utils.extensions.checkStatusImpl

interface StandardPermission : Permission {

    val permission: String

    override fun checkStatus(context: Context): Permission.Status {
        return checkStatusImpl(context)
    }
}