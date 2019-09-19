package com.arttttt.rotationcontrolv3.utils.delegates.preferences

import android.content.SharedPreferences

class PreferencesDelegate(
    private val prefs: SharedPreferences
): IPreferencesDelegate {

    /*companion object {
        private const val PREFERENCES_NAME = "settings"

        *//*enum class Parameters(val parameterName: String) {
            FIRST_BOOT("first_boot"),
            START_ON_BOOT("start_on_boot")
        }*//*
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun getBool(parameter: Parameters, defaultValue: Boolean = false) =
        preferences.getBoolean(parameter.parameterName, defaultValue)

    fun putBool(parameter: Parameters, value: Boolean) = preferences
        .edit()
        .putBoolean(parameter.parameterName, value)
        .apply()*/

    override fun getBool(name: String): Boolean {
        return prefs.getBoolean(name, false)
    }

    override fun putBool(name: String, value: Boolean) {
        prefs
            .edit()
            .putBoolean(name, value)
            .apply()
    }
}