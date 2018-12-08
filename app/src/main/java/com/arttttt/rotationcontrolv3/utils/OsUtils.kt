package com.arttttt.rotationcontrolv3.utils

import android.text.TextUtils
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

object OsUtils {
    fun isMiui() = !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"))

    private fun getSystemProperty(propName: String): String {
        var result = ""

        try {
            val process = Runtime.getRuntime().exec("getprop $propName")
            val reader = BufferedReader(InputStreamReader(process.inputStream))
            result = reader.readLine()
            reader.close()
            process.destroy()
        }
        catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }
}