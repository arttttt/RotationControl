package arttttt.rotationcontrol;

import android.content.Context;
import android.content.SharedPreferences;

public class RotationSettings {

    private final String ROTATION_PREFERENCES = "pref_rotation_control";

    SharedPreferences mSharedPreferences;

    public RotationSettings(Context context) {
        mSharedPreferences = context.getSharedPreferences(ROTATION_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void changePreference(String preferenceName, Object state){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(preferenceName, state.toString());
        editor.apply();
    }

    public String getString(String key, String defValue){
        return mSharedPreferences.getString(key, defValue);
    }
}
