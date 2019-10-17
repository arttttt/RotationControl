package com.arttttt.rotationcontrolv3.utils.delegates.preferences

interface IPreferencesDelegate {
    fun getBool(name: String): Boolean
    fun putBool(name: String, value: Boolean)

    fun getInt(name: String): Int?
    fun putInt(name: String, value: Int)
}