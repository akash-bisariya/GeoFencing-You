package com.example.akash.geofencingyou;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import static android.content.Context.AUDIO_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class GeofenceReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String message="";

            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            if(geofencingEvent.getGeofenceTransition()== Geofence.GEOFENCE_TRANSITION_ENTER)
            {
                message="Received Entering Appstudioz Event";
            }
            else if(geofencingEvent.getGeofenceTransition()== Geofence.GEOFENCE_TRANSITION_EXIT)
            {
                message="Received Exiting Appstudioz Event";
            }
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                            .setContentTitle("Received Geofence Notification")

                            .setContentText(message);

// Sets an ID for the notification
            int mNotificationId = 001;
// Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr =
                    (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
//            ((Vibrator)getSystemService(VIBRATOR_SERVICE)).vibrate(800);
            if(message.equals("Entering Appstudioz")) {
                ((AudioManager) context.getSystemService(AUDIO_SERVICE)).setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }
            else
            {
                ((AudioManager) context.getSystemService(AUDIO_SERVICE)).setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            }

        }





    }
}
