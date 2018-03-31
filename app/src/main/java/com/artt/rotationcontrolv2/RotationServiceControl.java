package com.artt.rotationcontrolv2;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

class RotationServiceControl {

    static void startRotationService(Context context){
        Intent intent = new Intent(context, RotationService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            context.startForegroundService(intent);
        else
            context.startService(intent);
    }

    static void stopRotationService(Context context){
        Intent intent = new Intent(context, RotationService.class);
        context.stopService(intent);
    }
}
