package com.example.akash.geofencingyou;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by akash on 24/3/17.
 */

public class GeoFenceService extends JobService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    GoogleApiClient mGoogleApiClient;
    Location mGeoLocation;
    Geofence mGeofence;
    com.firebase.jobdispatcher.JobParameters jobParameters;

    @Override
    public boolean onStartJob(com.firebase.jobdispatcher.JobParameters job) {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
        jobParameters = job;
        return false;
    }

    @Override
    public boolean onStopJob(com.firebase.jobdispatcher.JobParameters job) {
        mGoogleApiClient.disconnect();
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mGeoLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (mGeoLocation != null) {
            Log.e("GeoFenceService Current Location", "" + mGeoLocation.getLatitude() + " " + mGeoLocation.getLongitude());
            SharedPreferences sharedPreferences = getSharedPreferences("LocationLatLong", MODE_PRIVATE);
            if (sharedPreferences.getString("Latitude", "").equals("") && sharedPreferences.getString("Longitude", "").equals("")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("Latitude", String.valueOf(mGeoLocation.getLatitude()));
                editor.putString("Longitude", String.valueOf(mGeoLocation.getLongitude()));
                editor.apply();

                mGeofence = new Geofence.Builder()
                        .setRequestId("Appstudioz")
                        .setCircularRegion(mGeoLocation.getLatitude(), mGeoLocation.getLongitude(), 100)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                        .build();
                mGoogleApiClient.connect();

                Intent intent = new Intent(this, MyIntentServiceGeoFencing.class);
                PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
                builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

                builder.addGeofence(mGeofence);

                LocationServices.GeofencingApi.addGeofences(mGoogleApiClient, builder.build(), pendingIntent).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if (status.isSuccess()) {
                            // Successfully registered
                            Log.e("GeoFenceService", "Geofence Registered");
                            jobFinished(jobParameters, false);

                        } else if (status.hasResolution()) {
                            // Google provides a way to fix the issue
                    /*
                    status.startResolutionForResult(
                            mContext,     // your current activity used to receive the result
                            RESULT_CODE); // the result code you'll look for in your
                    // onActivityResult method to retry registering
                    */
                            Log.e("GeoFenceService", "Resolution Required: " + status.getStatusMessage());
                        } else {
                            // No recovery. Weep softly or inform the user.
                            Log.e("GeoFenceService", "Registering failed: " + status.getStatusMessage());
                        }
                    }
                });

            }
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("GeoFenceService", "Location onConnectionSuspended");
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("GeoFenceService", "Location onConnectionFailed");
    }

}
