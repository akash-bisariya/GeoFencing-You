package com.example.akash.geofencingyou;

import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RelativeLayout constraintLayout = (RelativeLayout) findViewById(R.id.cl_home);
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        } else {
            FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(MainActivity.this));
            Job job = firebaseJobDispatcher.newJobBuilder()
                    .setService(GeoFenceService.class)
                    .setTag("Geofence")
                    .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                    .setRecurring(false)
                    .setTrigger(Trigger.executionWindow(0, 0))
                    .setReplaceCurrent(true)
                    .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                    .setConstraints(
                            Constraint.ON_ANY_NETWORK
                    )
                    .build();
            firebaseJobDispatcher.mustSchedule(job);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FirebaseJobDispatcher firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(MainActivity.this));
        Job job = firebaseJobDispatcher.newJobBuilder()
                .setService(GeoFenceService.class)
                .setTag("Geofence")
                .setLifetime(Lifetime.UNTIL_NEXT_BOOT)
                .setTag("DeviceGeoFenceService")
                .setRecurring(false)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(0, 0))
                .setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL)
                .setConstraints(
                        Constraint.ON_ANY_NETWORK
                )
                .build();

        firebaseJobDispatcher.mustSchedule(job);
    }

    @Override
    protected void onResume() {
        super.onResume();
        animationDrawable.start();
    }
}
