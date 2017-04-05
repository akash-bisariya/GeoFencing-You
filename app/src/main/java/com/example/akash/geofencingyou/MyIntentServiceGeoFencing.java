package com.example.akash.geofencingyou;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentServiceGeoFencing extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.example.akash.constraintlayoutexa.action.FOO";
    private static final String ACTION_BAZ = "com.example.akash.constraintlayoutexa.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.example.akash.constraintlayoutexa.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.example.akash.constraintlayoutexa.extra.PARAM2";

    public MyIntentServiceGeoFencing() {
        super("MyIntentServiceGeoFencing");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentServiceGeoFencing.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionBaz(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentServiceGeoFencing.class);
        intent.setAction(ACTION_BAZ);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String message="";
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if(geofencingEvent.getGeofenceTransition()== Geofence.GEOFENCE_TRANSITION_ENTER)
            {
                message="Entering Appstudioz";
            }
            else if(geofencingEvent.getGeofenceTransition()== Geofence.GEOFENCE_TRANSITION_EXIT)
            {
                message="Exiting Appstudioz";
            }
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                            .setContentTitle("Geofence Notification")
                            .setContentText(message);

// Sets an ID for the notification
            int mNotificationId = 001;
// Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
//            ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(800);
            if(message.equals("Entering Appstudioz")) {
                ((AudioManager) getSystemService(AUDIO_SERVICE)).setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
            else
            {
                ((AudioManager) getSystemService(AUDIO_SERVICE)).setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }

        }
    }


}
