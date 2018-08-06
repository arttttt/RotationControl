package com.artt.rotationcontrolv2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.provider.Settings.System;
import android.support.annotation.Nullable;
import android.view.Surface;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.widget.Toast;

public class RotationService extends Service implements AccelerometerRotationObserver.Callback {

    public static boolean isStarted = false;

    //private static final String TAG = "RotationControlV2";
    private static final String INTENT_CLICKED_BUTTON_ID = "BUTTON_ID";
    private static final String INTENT_ACTION = "ACTION";
    private static final String NOTIFICATION_CHANNEL = "Rotation Control";
    private static final String NOTIFICATION_CHANNEL_ID = "rotation_service_notification_channel_id";

    private static final int NOTIFICATION_ID = 999;
    private static final int ACTION_CLICK = 2;
    private static final int ORIENTATION_PORTRAIT = Surface.ROTATION_0;
    private static final int ORIENTATION_LANDSCAPE = Surface.ROTATION_90;
    private static final int ORIENTATION_PORTRAIT_REVERSE = Surface.ROTATION_180;
    private static final int ORIENTATION_LANDSCAPE_REVERSE = Surface.ROTATION_270;
    private static final int ORIENTATION_AUTO = 4;
    private final int[] IDS_BUTTON = new int[]{R.id.btn_auto, R.id.btn_portrait, R.id.btn_portrait_reverse,
            R.id.btn_landscape, R.id.btn_landscape_reverse};

    private int mScreenOrientation;
    private AccelerometerRotationObserver mAccelerometerRotationObserver;

    @Override
    public void update() {
        if (mScreenOrientation != ORIENTATION_AUTO && PermissionManager.CanWriteSettings(getApplicationContext()))
            System.putInt(getContentResolver(), System.ACCELEROMETER_ROTATION, 0);
    }

    private void setOrientation(int orientation){
        if (!PermissionManager.CanWriteSettings(getApplicationContext())) {
            Toast.makeText(this, R.string.service_write_denied, Toast.LENGTH_LONG).show();
            return;
        }

        if (mScreenOrientation != ORIENTATION_AUTO) {
            System.putInt(getContentResolver(), System.USER_ROTATION, orientation);

            try {
                if (System.getInt(getContentResolver(), System.ACCELEROMETER_ROTATION) == 1)
                    System.putInt(getContentResolver(), System.ACCELEROMETER_ROTATION, 0);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        }
        else
            System.putInt(getContentResolver(), System.ACCELEROMETER_ROTATION, 1);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_HIGH);
            if (manager != null)
                manager.createNotificationChannel(channel);
        }

        WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        if (manager != null)
            mScreenOrientation = manager.getDefaultDisplay().getRotation();
        else {
            stopSelf();
            return;
        }

        mAccelerometerRotationObserver = new AccelerometerRotationObserver(this);
        getContentResolver().registerContentObserver(System.getUriFor(System.ACCELEROMETER_ROTATION),
                false, mAccelerometerRotationObserver);

        createNotification(mScreenOrientation);

        isStarted = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getContentResolver().unregisterContentObserver(mAccelerometerRotationObserver);

        isStarted = false;
    }

    private void onClick(int id){
        switch (id){
            case R.id.btn_auto:{
                mScreenOrientation = ORIENTATION_AUTO;
                break;
            }
            case R.id.btn_portrait:{
                mScreenOrientation = ORIENTATION_PORTRAIT;
                break;
            }
            case R.id.btn_portrait_reverse:{
                mScreenOrientation = ORIENTATION_PORTRAIT_REVERSE;
                break;
            }
            case R.id.btn_landscape: {
                mScreenOrientation = ORIENTATION_LANDSCAPE;
                break;
            }
            case R.id.btn_landscape_reverse:{
                mScreenOrientation = ORIENTATION_LANDSCAPE_REVERSE;
                break;
            }
        }
        createNotification(mScreenOrientation);
        setOrientation(mScreenOrientation);
    }

    private void createNotification(int orientation) {
        RemoteViews notificationContent = new RemoteViews(getPackageName(), R.layout.notification);
        PendingIntent activity = PendingIntent.getActivity(this, 0,
                new Intent(this, SettingsActivity.class), Intent.FILL_IN_COMPONENT);

        Intent intent = new Intent(this, RotationService.class);
        intent.putExtra(INTENT_ACTION, ACTION_CLICK);
        for (int length = IDS_BUTTON.length, i = 0; i < length; ++i) {
            int id = IDS_BUTTON[i];
            intent.putExtra(INTENT_CLICKED_BUTTON_ID, id);
            notificationContent.setOnClickPendingIntent(id, PendingIntent.getService(this, id,
                    intent, PendingIntent.FLAG_UPDATE_CURRENT));
            notificationContent.setInt(id, "setBackgroundResource", android.R.color.transparent);
            notificationContent.setInt(id, "setColorFilter", getResources().getColor(R.color.btn_active));
        }

        int smallIconId = -1;
        int buttonId = -1;
        switch (orientation){
            case ORIENTATION_AUTO:{
                buttonId = R.id.btn_auto;
                smallIconId = R.drawable.ic_stat_notify_auto;
                break;
            }
            case ORIENTATION_PORTRAIT: {
                buttonId = R.id.btn_portrait;
                smallIconId = R.drawable.ic_stat_notify_portrait;
                break;
            }
            case ORIENTATION_PORTRAIT_REVERSE:{
                buttonId = R.id.btn_portrait_reverse;
                smallIconId = R.drawable.ic_stat_notify_portrait_reverse;
                break;
            }
            case ORIENTATION_LANDSCAPE: {
                buttonId = R.id.btn_landscape;
                smallIconId = R.drawable.btn_landscape;
                break;
            }
            case ORIENTATION_LANDSCAPE_REVERSE: {
                buttonId = R.id.btn_landscape_reverse;
                smallIconId = R.drawable.btn_landscape_reverse;
                break;
            }
        }

        notificationContent.setInt(buttonId, "setBackgroundResource", R.drawable.btnback_active);
        notificationContent.setInt(buttonId, "setColorFilter", android.R.color.transparent);

        Notification notification;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setCustomContentView(notificationContent)
                    .setContentIntent(activity)
                    .setSmallIcon(smallIconId)
                    .build();
            notification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        }
        else
            notification = new Notification.Builder(this)
                    .setContent(notificationContent)
                    .setContentIntent(activity)
                    .setSmallIcon(smallIconId)
                    .setPriority(Notification.PRIORITY_MAX)
                    .build();


        showNotification(notification);
    }

    private void showNotification(Notification notification){
        startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int action = intent.getIntExtra(INTENT_ACTION, 0);
            switch (action){
                case ACTION_CLICK:
                    onClick(intent.getIntExtra(INTENT_CLICKED_BUTTON_ID, 0));
            }
        }

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}