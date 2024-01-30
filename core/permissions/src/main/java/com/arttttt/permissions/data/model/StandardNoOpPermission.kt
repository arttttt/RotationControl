package com.arttttt.permissions.data.model

import android.content.Context
import com.arttttt.permissions.domain.entity.Permission
import com.arttttt.permissions.domain.entity.StandardPermission

data object StandardNoOpPermission : StandardPermission {

    override val permission: String = ""

    override fun checkStatus(context: Context): Permission.Status {
        return Permission.Status.Granted
    }
}