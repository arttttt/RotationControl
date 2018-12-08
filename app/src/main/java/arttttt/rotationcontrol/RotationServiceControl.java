package arttttt.rotationcontrol;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.List;

class RotationServiceControl {

    private Context mContext;

    RotationServiceControl(Context context) {
        mContext = context;
    }

    void startRotationService(){
        Intent intent = new Intent(mContext, RotationService.class);
        mContext.startService(intent);
    }

    void stopRotationService(){
        Intent intent = new Intent(mContext, RotationService.class);
        mContext.stopService(intent);
    }

    boolean isServiceStarted(){
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(50);

        for (int i = 0; i < rs.size(); i++) {
            if (rs.get(i).service.getClassName().equals(RotationService.class.getName())){
                return true;
            }
        }
        return false;
    }
}
