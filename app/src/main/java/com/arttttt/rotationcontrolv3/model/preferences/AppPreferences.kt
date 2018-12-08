package com.arttttt.rotationcontrolv3.model.preferences

import android.content.Context
import android.content.SharedPreferences

class AppPreferences(context: Context) {

    companion object {
        enum class Parameters(val parameterName: String) {
            FIRST_BOOT("first_boot"),
            START_ON_BOOT("start_on_boot")
        }
    }

    private val PREFERENCES_NAME = "settings"
    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getBool(parameter: Parameters, defaultValue: Boolean = false) =
        preferences.getBoolean(parameter.parameterName, defaultValue)

    fun putBool(parameter: Parameters, value: Boolean) = preferences
        .edit()
        .putBoolean(parameter.parameterName, value)
        .apply()
}