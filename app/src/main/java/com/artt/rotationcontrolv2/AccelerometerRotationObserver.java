package com.artt.rotationcontrolv2;

import android.database.ContentObserver;

public class AccelerometerRotationObserver extends ContentObserver {

    private static final String TAG = "RotationControlV2";

    Callback mCallback;

    public AccelerometerRotationObserver(Callback callback) {
        super(null);

        mCallback = callback;
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        if (mCallback != null)
            mCallback.update();
    }

    public interface Callback {
        void update();
    }
}
