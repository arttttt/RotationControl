package com.arttttt.rotationcontrolv3.utils.delegates.preferences

import android.content.SharedPreferences

class PreferencesDelegate(
    private val prefs: SharedPreferences
): IPreferencesDelegate {

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