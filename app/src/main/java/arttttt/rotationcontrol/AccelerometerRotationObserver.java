package arttttt.rotationcontrol;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings.System;

public class AccelerometerRotationObserver extends ContentObserver {

    public static final int ORIENTATION_AUTO = -1;
    private static final int ORIENTATION_PORTRAIT = 0;
    private static final int ORIENTATION_LANDSCAPE = 1;
    private static final int ORIENTATION_PORTRAIT_REVERSE = 2;
    private static final int ORIENTATION_LANDSCAPE_REVERSE = 3;

    private RotationService mRotationService;
    private int mTurnedOffRotation;
    private ContentResolver mContentResolver;
    private int mOrientationMode;

    AccelerometerRotationObserver(RotationService rotationService, Handler handler) {
        super(handler);
        mRotationService = rotationService;
        mContentResolver = mRotationService.getContentResolver();
        mContentResolver.registerContentObserver(System.getUriFor("accelerometer_rotation"), false, this);
        mTurnedOffRotation = System.getInt(mRotationService.getContentResolver(), "accelerometer_rotation", 0);
    }

    public void onOrientationChanged(int mode){
        mOrientationMode = mode;
        switch (mode){
            case ORIENTATION_PORTRAIT:
            case ORIENTATION_PORTRAIT_REVERSE:
            case ORIENTATION_LANDSCAPE:
            case ORIENTATION_LANDSCAPE_REVERSE:
                System.putInt(mContentResolver, "accelerometer_rotation", 0);
                break;
            case ORIENTATION_AUTO:
                System.putInt(mContentResolver, "accelerometer_rotation", 1);
                break;
        }
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        if (mTurnedOffRotation != System.getInt(mContentResolver, "accelerometer_rotation", 0) && mOrientationMode != ORIENTATION_AUTO){
            System.putInt(mRotationService.getContentResolver(), "accelerometer_rotation", 0);
        }
    }
}
