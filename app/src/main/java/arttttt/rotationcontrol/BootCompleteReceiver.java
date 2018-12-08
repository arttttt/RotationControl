package arttttt.rotationcontrol;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.miui.rotationcontrol.R;

public class BootCompleteReceiver extends BroadcastReceiver {

    private final String APP_PREFERENCES = "pref_rotation_control";
    private String pref_start_on_boot;

    @Override
    public void onReceive(Context context, Intent intent) {

        pref_start_on_boot = context.getResources().getString(R.string.prefkey_start_on_boot);

        boolean startOnBoot = Boolean.valueOf(context.getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE).getString(pref_start_on_boot, "false"));

        if (startOnBoot){
            Intent serviceIntent = new Intent(context, RotationService.class);
            context.startService(serviceIntent);
        }
    }
}
