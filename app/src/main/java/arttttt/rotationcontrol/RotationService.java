package arttttt.rotationcontrol;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;
import android.view.WindowManager;
import android.widget.RemoteViews;
import android.provider.Settings.System;

import com.miui.rotationcontrol.R;

public class RotationService extends Service {

    public static final int ORIENTATION_AUTO = -1;
    private static final int ORIENTATION_PORTRAIT = 0;
    private static final int ORIENTATION_LANDSCAPE = 1;
    private static final int ORIENTATION_PORTRAIT_REVERSE = 2;
    private static final int ORIENTATION_LANDSCAPE_REVERSE = 3;

    private static final int ACTION_CLICK = 2;

    private static final String INTENT_CLICKED_BUTTON_ID = "BUTTON_ID";
    private static final String INTENT_ACTION = "ACTION";

    private String pref_orientation;

    private Notification mNotification;
    private RemoteViews mNotificationContent;
    private int mScreenOrientation;
    private RotationSettings mRotationSettings;
    private AccelerometerRotationObserver mAccelerometerRotationObserver;

    private static final int NOTIFICATION_ID = 999;
    private final int[] IDS_BUTTON = new int[]{R.id.btn_auto, R.id.btn_portrait, R.id.btn_portrait_reverse, R.id.btn_landscape, R.id.btn_landscape_reverse};

    @Override
    public void onCreate() {
        super.onCreate();

        pref_orientation = getResources().getString(R.string.prefkey_orientation);

        mRotationSettings = new RotationSettings(this);

        mScreenOrientation = Integer.valueOf(mRotationSettings.getString(pref_orientation, String.valueOf(((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getOrientation())));

        mAccelerometerRotationObserver = new AccelerometerRotationObserver(this, new Handler());

        createNotification();
        updateNotification();
        setOrientation();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        getContentResolver().unregisterContentObserver(mAccelerometerRotationObserver);
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
        setOrientation();
        updateNotification();
        saveOrientation();
    }

    private void setOrientation(){
        ContentResolver contentResolver = getContentResolver();
        mAccelerometerRotationObserver.onOrientationChanged(mScreenOrientation);
        System.putInt(contentResolver, "user_rotation", mScreenOrientation);
    }

    private void createNotification(){
        if (this.mNotification == null) {
            mNotificationContent = new RemoteViews(getPackageName(), R.layout.notification);
            PendingIntent activity = PendingIntent.getActivity(this, 0, new Intent(this, PreferenceActivity.class), Intent.FILL_IN_COMPONENT);

            Intent intent = new Intent(this, RotationService.class);
            intent.putExtra(INTENT_ACTION, ACTION_CLICK);
            for (int length = IDS_BUTTON.length, i = 0; i < length; ++i) {
                int id = IDS_BUTTON[i];
                intent.putExtra(INTENT_CLICKED_BUTTON_ID, id);
                mNotificationContent.setOnClickPendingIntent(id, PendingIntent.getService(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }

            int smallIconId = -1;
            switch (mScreenOrientation){
                case ORIENTATION_AUTO:{
                    smallIconId = R.drawable.ic_stat_notify_auto;
                    break;
                }
                case ORIENTATION_PORTRAIT: {
                    smallIconId = R.drawable.ic_stat_notify_portrait;
                    break;
                }
                case ORIENTATION_PORTRAIT_REVERSE:{
                    smallIconId = R.drawable.ic_stat_notify_portrait_reverse;
                    break;
                }
                case ORIENTATION_LANDSCAPE: {
                    smallIconId = R.drawable.btn_landscape;
                    break;
                }
                case ORIENTATION_LANDSCAPE_REVERSE: {
                    smallIconId = R.drawable.btn_landscape_reverse;
                    break;
                }
            }

            this.mNotification = new Builder(this).
                    setContent(mNotificationContent).
                    setContentIntent(activity).
                    setSmallIcon(smallIconId).
                    setContentText("TEST TEXT").
                    setPriority(Notification.PRIORITY_MAX).
                    build();

            showNotification();
        }
    }

    private void saveOrientation(){
        mRotationSettings.changePreference(pref_orientation, mScreenOrientation);
    }

    private void updateNotification(){
        for (int length = IDS_BUTTON.length, i = 0; i < length; ++i) {
            int id = IDS_BUTTON[i];
            mNotificationContent.setInt(id, "setBackgroundResource", android.R.color.transparent);
            mNotificationContent.setInt(id, "setColorFilter", getResources().getInteger(R.integer.color_background_inactive_filter));
        }
        int buttonId = -1;
        int modeDescriptionId = -1;
        int smallIconId = -1;
        switch (mScreenOrientation){
            case ORIENTATION_AUTO:{
                buttonId = R.id.btn_auto;
                smallIconId = R.drawable.ic_stat_notify_auto;
                modeDescriptionId = R.string.auto_mode;
                break;
            }
            case ORIENTATION_PORTRAIT: {
                buttonId = R.id.btn_portrait;
                modeDescriptionId = R.string.portrait_mode;
                smallIconId = R.drawable.ic_stat_notify_portrait;
                break;
            }
            case ORIENTATION_PORTRAIT_REVERSE:{
                buttonId = R.id.btn_portrait_reverse;
                modeDescriptionId = R.string.portrait_reverse_mode;
                smallIconId = R.drawable.ic_stat_notify_portrait_reverse;
                break;
            }
            case ORIENTATION_LANDSCAPE: {
                buttonId = R.id.btn_landscape;
                modeDescriptionId = R.string.landscape_mode;
                smallIconId = R.drawable.btn_landscape;
                break;
            }
            case ORIENTATION_LANDSCAPE_REVERSE: {
                buttonId = R.id.btn_landscape_reverse;
                modeDescriptionId = R.string.landscape_reverse_mode;
                smallIconId = R.drawable.btn_landscape_reverse;
                break;
            }
        }
        mNotificationContent.setInt(buttonId, "setBackgroundResource", R.drawable.btnback_active);
        mNotificationContent.setInt(buttonId, "setColorFilter", android.R.color.transparent);
        mNotificationContent.setInt(R.id.rotationMode, "setText", modeDescriptionId);
        mNotification.icon = smallIconId;

        showNotification();
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
        else{
            updateNotification();
        }
        return START_STICKY;
    }

    private void showNotification(){
        startForeground(NOTIFICATION_ID, mNotification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
