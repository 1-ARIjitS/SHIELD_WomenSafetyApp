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
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


import com.example.womensafety.Activities.AdminActivity;
import com.example.womensafety.Activities.NextToKinActivity;
import com.example.womensafety.Activities.SuspectRegistrationActivity;
import com.example.womensafety.Activities.TravelLog;
import com.google.android.material.navigation.NavigationView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Detail_Forms extends AppCompatActivity {

    Button Access_DisplayTime;
    private int CalenderHour, CalenderMinute;
    String format;
    Calendar calender;
    TimePickerDialog timePickerDialog;

    Button VehicleImageSelector;
    public static final int GET_FROM_GALLERY = 3;
    ImageView imageView;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView tv_latitude, tv_longitude;
    Double latitude, longitude;
    EditText et_vehicleNumber, et_travellingTo, et_travellingFrom;
    TextView tv_estimatedTime;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    Intent intent;

    //Travel Log passing values
    String vehicleNumber, travellingTo, travellingFrom, timeStarted, timeReached, address, date, estimatedTime;
    Uri vehicleImageUri;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__forms);

        tv_latitude = findViewById(R.id.tv_latitude);
        tv_longitude = findViewById(R.id.tv_longitude);
        tv_estimatedTime = findViewById(R.id.estimatedTime);

        et_vehicleNumber = findViewById(R.id.vehicleNumber);
        et_travellingFrom = findViewById(R.id.travelingFrom);
        et_travellingTo = findViewById(R.id.travelingTo);

        vehicleNumber = et_vehicleNumber.getText().toString();
        travellingFrom = et_travellingFrom.getText().toString();
        travellingTo = et_travellingTo.getText().toString();


        findViewById(R.id.startButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timeStarted = getCurrentTime();

                calender = Calendar.getInstance();
                date = DateFormat.getDateInstance(DateFormat.FULL).format(calender.getTime());

                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(
                            Detail_Forms.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            REQUEST_CODE_LOCATION_PERMISSION
                    );
                }
                else{
                    startLocationService();
                }
            }
        });

        findViewById(R.id.reachedSafelyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopLocationService();
                timeReached = getCurrentTime();
                address = getLocation();
                vehicleNumber = et_vehicleNumber.getText().toString();
                travellingFrom = et_travellingFrom.getText().toString();
                travellingTo = et_travellingTo.getText().toString();
                fn_reachedSafely();
            }
        });

        Access_DisplayTime = findViewById(R.id.timePicker);


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
                        tv_estimatedTime.setVisibility(View.VISIBLE);
                        tv_estimatedTime.setText("Estimated time: " + hourOfDay + ":" + minute + " " + format);
                        estimatedTime = hourOfDay + ":" + minute + " " + format;
                    }
                }, CalenderHour, CalenderMinute, false );
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
                        startActivity(new Intent(Detail_Forms.this, SuspectRegistrationActivity.class));
                        break;
                    case R.id.nav_nextToKin:
                        startActivity(new Intent(Detail_Forms.this, NextToKinActivity.class));
                        break;
                    case R.id.nav_travelLog:
                        startActivity(new Intent(Detail_Forms.this, TravelLog.class));
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


            tv_latitude.setText(latitude+"");
            tv_longitude.setText(longitude+"");
        }
    };

    private void fn_reachedSafely(){

        intent = new Intent(Detail_Forms.this, TravelLog.class);
        intent.putExtra("vehicleNumber", vehicleNumber+"");
        intent.putExtra("travellingFrom", travellingFrom+"");
        intent.putExtra("travellingTo", travellingTo+"");
        intent.putExtra("date", date+"");
        intent.putExtra("timeStarted", timeStarted+"");
        intent.putExtra("timeReached", timeReached+"");
        intent.putExtra("address", address+"");
        intent.putExtra("vehicleImageUri", vehicleImageUri);
        intent.putExtra("estimatedTime", estimatedTime+"");
        startActivity(intent);
    }

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
        if(requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startLocationService();
            }
            else{
                Toast.makeText(this,"Permission denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isLocationServiceRunning(){
        ActivityManager activityManager =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if(activityManager != null){
            for(ActivityManager.RunningServiceInfo service:
            activityManager.getRunningServices(Integer.MAX_VALUE)){
                if(LocationService.class.getName().equals(service.service.getClassName())){
                    if(service.foreground){
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(LocationConstants.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this,"Tracking Starts",Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationService(){
        if(isLocationServiceRunning()){
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(LocationConstants.ACTION_STOP_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this,"Tracking Stops",Toast.LENGTH_SHORT).show();
        }
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK){

            imageView = findViewById(R.id.vehicleImage);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(data.getData());
            vehicleImageUri = data.getData();
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

    private String getCurrentTime(){
        return new SimpleDateFormat("hh:mm", Locale.getDefault()).format(new Date());
    }

    private String getLocation(){

        try{
            Geocoder geocoder = new Geocoder(Detail_Forms.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            return address;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
