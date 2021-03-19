package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;
import android.widget.Toast;


import com.example.womensafety.Activities.AboutUsActivity;
import com.example.womensafety.Activities.AdminActivity;
import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.Activities.NextToKinActivity;
import com.example.womensafety.Activities.NextTokinListActivity;
import com.example.womensafety.Activities.SuspectListActivity;
import com.example.womensafety.Activities.SuspectRegistrationActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class Detail_Forms extends AppCompatActivity {

    Button Access_DisplayTime;
    private int CalenderHour, CalenderMinute;
    String format;
    Calendar calender;
    TimePickerDialog timePickerDialog;
    Button VehicleImageSelector;
    public static final int GET_FROM_GALLERY = 3;
    ImageView imageView;
    TextView timeTextView;

    FirebaseAuth auth;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView tv_latitude, tv_longitude;
    Double latitude, longitude;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__forms);

        tv_latitude = findViewById(R.id.tv_latitude);
        tv_longitude = findViewById(R.id.tv_longitude);

        auth = FirebaseAuth.getInstance();


        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            Detail_Forms.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                } else {
                    startLocationService();
                }
            }
        });

        findViewById(R.id.reachedSafelyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLocationService();
            }
        });

        Access_DisplayTime = findViewById(R.id.timePicker);
        timeTextView = findViewById(R.id.timeTextView);


        Access_DisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calender = Calendar.getInstance();
                CalenderHour = calender.get(Calendar.HOUR_OF_DAY);
                CalenderMinute = calender.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(Detail_Forms.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay == 0) {
                            hourOfDay += 12;
                            format = "AM";
                        } else if (hourOfDay == 12) {
                            format = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        timeTextView.setVisibility(View.VISIBLE);
                        timeTextView.setText("Estimated time: " + hourOfDay + ":" + minute + " " + format);
                    }
                }, CalenderHour, CalenderMinute, false);
                timePickerDialog.show();
            }
        });

        VehicleImageSelector = findViewById(R.id.vehicleImageSelector);
        VehicleImageSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }

        });

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(Detail_Forms.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(Detail_Forms.this, SuspectListActivity.class));
                        break;
                    case R.id.nav_nextToKin:
                        startActivity(new Intent(Detail_Forms.this, NextTokinListActivity.class));
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(Detail_Forms.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(Detail_Forms.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            latitude = Double.valueOf(intent.getStringExtra("latitude"));
            longitude = Double.valueOf(intent.getStringExtra("longitude"));


            tv_latitude.setText(latitude + "");
            tv_longitude.setText(longitude + "");
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(LocationService.str_receiver));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning() {
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo service :
                    activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(service.service.getClassName())) {
                    if (service.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(LocationConstants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Tracking Starts", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationService() {
        if (isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(LocationConstants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Tracking Stops", Toast.LENGTH_SHORT).show();
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            imageView = findViewById(R.id.vehicleImage);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(data.getData());
        }
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
    }

}
