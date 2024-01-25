package com.arttttt.permissions.domain.entity

import android.content.Context
import android.content.Intent

interface IntentPermission : Permission {

    fun createIntent(context: Context): Intent
}