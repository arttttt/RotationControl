package com.arttttt.rotationcontrolv3.data.model

import android.content.Context
import android.content.Intent
import com.arttttt.permissions.domain.entity.IntentPermission
import com.arttttt.permissions.domain.entity.Permission

data object IntentNoOpPermission : IntentPermission {

    override fun createIntent(context: Context): Intent {
        return Intent()
    }

    override fun checkStatus(context: Context): Permission.Status {
        return Permission.Status.Granted
    }
}