package arttttt.rotationcontrol;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;

import com.miui.rotationcontrol.R;

public class PreferenceActivity extends android.preference.PreferenceActivity implements Preference.OnPreferenceChangeListener {

    private CheckBoxPreference mStartService, mStartServiceOnBoot;
    private boolean mEnableStartService, mEnableStartServiceOnBoot;
    private String pref_start_service, pref_start_on_boot;
    private RotationServiceControl mRotationServiceControl;
    private RotationSettings mRotationSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferencee_main);

        pref_start_service = getResources().getString(R.string.prefkey_start_service);
        pref_start_on_boot = getResources().getString(R.string.prefkey_start_on_boot);

        mStartService = (CheckBoxPreference) findPreference(pref_start_service);
        mStartServiceOnBoot = (CheckBoxPreference) findPreference(pref_start_on_boot);

        mStartService.setOnPreferenceChangeListener(this);
        mStartServiceOnBoot.setOnPreferenceChangeListener(this);

        mRotationServiceControl = new RotationServiceControl(this);

        mRotationSettings = new RotationSettings(this);
    }

    @Override
    protected void onResume() {
        mEnableStartService = mRotationServiceControl.isServiceStarted();
        mEnableStartServiceOnBoot = Boolean.valueOf(mRotationSettings.getString(pref_start_on_boot, "false"));
        mStartService.setChecked(mEnableStartService);
        mStartServiceOnBoot.setChecked(mEnableStartServiceOnBoot);
        super.onResume();
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mStartService){
            mRotationSettings.changePreference(pref_start_service, newValue);
            if (!mRotationServiceControl.isServiceStarted()){
                mRotationServiceControl.startRotationService();
            }
            else {
                mRotationServiceControl.stopRotationService();
            }
        }
        if (preference == mStartServiceOnBoot){
            mRotationSettings.changePreference(pref_start_on_boot, newValue);
        }
        return true;
    }
}
