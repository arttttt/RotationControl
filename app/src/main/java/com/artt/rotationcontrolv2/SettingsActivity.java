package com.artt.rotationcontrolv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SettingsActivity extends AppCompatActivity {

    //private static final String TAG = "RotationControlV2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        PermissionManager.RequestWriteSettingsPermission(this);

        SettingsFragment fragment = SettingsFragment.getInstance(null);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit();
    }
}
