package com.artt.rotationcontrolv2;

import android.content.Context;
import android.content.Intent;

class RotationServiceControl {

    static void startRotationService(Context context){
        Intent intent = new Intent(context, RotationService.class);
        context.startService(intent);
    }

    static void stopRotationService(Context context){
        Intent intent = new Intent(context, RotationService.class);
        context.stopService(intent);
    }
}
