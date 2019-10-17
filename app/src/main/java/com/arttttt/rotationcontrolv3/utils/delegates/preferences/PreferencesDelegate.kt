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

    override fun getInt(name: String): Int? {
        val result = prefs.getInt(name, Int.MIN_VALUE)

        return if (result == Int.MIN_VALUE) {
            null
        } else {
            result
        }
    }

    override fun putInt(name: String, value: Int) {
        prefs
            .edit()
            .putInt(name, value)
            .apply()
    }
}