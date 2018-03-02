package com.artt.rotationcontrolv2;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompletedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (PreferencesManager.getBool(context, SettingsFragment.PREF_AUTOSTART))
            RotationServiceControl.startRotationService(context);
    }
}