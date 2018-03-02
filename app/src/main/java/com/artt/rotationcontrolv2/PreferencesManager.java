package com.artt.rotationcontrolv2;

import android.content.Context;
import android.content.SharedPreferences;

class PreferencesManager {

    private static final String PREFS_NAME = "main_preferences";

    static void setValue(Context context, String valueKey, Object value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value instanceof Boolean)
            editor.putBoolean(valueKey, (Boolean) value);
        else if (value instanceof Float)
            editor.putFloat(valueKey, (Float) value);
        else if (value instanceof Integer)
            editor.putInt(valueKey, (Integer) value);
        else if (value instanceof Long)
            editor.putLong(valueKey, (Long) value);
        else if (value instanceof String)
            editor.putString(valueKey, (String) value);

        editor.apply();
    }

    static Boolean getBool(Context context, String valueKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(valueKey, false);
    }
}