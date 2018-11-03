package com.artt.rotationcontrolv2;

import android.os.Bundle;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener {

    public static final String PREF_AUTOSTART = "pref_autostart";
    private static final String TAG = "RotationControlV2";

    private CheckBoxPreference mStartService;
    private CheckBoxPreference mStartOnBoot;

    private static SettingsFragment sInstance;

    public static SettingsFragment getInstance(Bundle args) {

        if (sInstance == null)
            sInstance = new SettingsFragment();

        if (args != null)
            sInstance.setArguments(args);

        return sInstance;
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_main);

        mStartService = (CheckBoxPreference) findPreference(getString(R.string.start_service_pref_key));
        mStartService.setChecked(RotationService.isStarted);
        mStartOnBoot = (CheckBoxPreference) findPreference(getString(R.string.start_on_boot_pref_key));
        mStartOnBoot.setChecked(PreferencesManager.getBool(getContext(), PREF_AUTOSTART));

        mStartService.setOnPreferenceChangeListener(this);
        mStartOnBoot.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mStartService) {
            if ((Boolean) newValue)
                RotationServiceControl.startRotationService(getContext());
            else
                RotationServiceControl.stopRotationService(getContext());
        } else if (preference == mStartOnBoot) {
            PreferencesManager.setValue(getContext(), PREF_AUTOSTART, newValue);
        }
        return true;
    }
}
