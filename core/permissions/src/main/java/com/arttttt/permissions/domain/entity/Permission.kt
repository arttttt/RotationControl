package com.arttttt.permissions.domain.entity

import android.content.Context

interface Permission {

    sealed class Status {

        companion object;

        data object Granted : Status()
        data object Denied : Status()
    }

    val title: String

    fun checkStatus(context: Context): Status
}