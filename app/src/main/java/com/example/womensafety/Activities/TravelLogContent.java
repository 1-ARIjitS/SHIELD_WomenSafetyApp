package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.womensafety.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.womensafety.User.Detail_Forms;
import com.google.android.material.navigation.NavigationView;

public class TravelLogContent extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView textDate, textVehicleNumber, textTravellingFrom, textTravellingTo, textTimeStarted, textTimeExpected, textTimeReached, textAddress;

    ImageView vehicleImage;

    View hView;
    TextView Username;


    Uri vehicleImageUri;

    String vehicleNumber, travellingTo, travellingFrom, timeStarted, timeReached, address, date, estimatedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_log_content);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        textDate = findViewById(R.id.disp_date);
        textVehicleNumber = findViewById(R.id.disp_vehicleNumber);
        textTravellingFrom = findViewById(R.id.disp_travellingFrom);
        textTravellingTo = findViewById(R.id.disp_travellingTo);
        textTimeStarted = findViewById(R.id.disp_startedTime);
        textTimeExpected = findViewById(R.id.disp_expectedTime);
        textTimeReached = findViewById(R.id.disp_reachedTime);
        textAddress = findViewById(R.id.disp_lastLocation);

        vehicleImage = findViewById(R.id.disp_vehicleImage);

        if(bundle != null){
            address = (String) bundle.get("address");
            timeReached = (String) bundle.get("timeStarted");
            timeStarted = (String) bundle.get("timeReached");
            date = (String) bundle.get("date");
            estimatedTime = (String) bundle.get("estimatedTime");
            vehicleNumber = (String) bundle.get("vehicleNumber");
            travellingFrom = (String) bundle.get("travellingFrom");
            travellingTo = (String) bundle.get("travellingTo");
            vehicleImageUri = (Uri) bundle.get("vehicleImageUri") ;

            textDate.setText(date + "");
            textVehicleNumber.setText(vehicleNumber + "");
            textTimeStarted.setText(timeStarted + "");
            textTimeReached.setText(timeReached + "");
            textTimeExpected.setText(estimatedTime + "");
            textAddress.setText(address + "");
            textTravellingFrom.setText(travellingFrom + "");
            textTravellingTo.setText(travellingTo + "");
            vehicleImage.setImageURI(vehicleImageUri);
        }


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        hView=navigationView.getHeaderView(0);
        Username=hView.findViewById(R.id.header_username);
        String user=getIntent().getStringExtra("use");
        Username.setText(user);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_travelLog:
                        startActivity(new Intent(TravelLogContent.this, TravelLog.class));
                        break;

                    case R.id.nav_home:
                        startActivity(new Intent(TravelLogContent.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(TravelLogContent.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(TravelLogContent.this, SuspectRegistrationActivity.class));
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(TravelLogContent.this, SettingsActivity.class));
                        break;

                    case R.id.nav_nextToKin:
                        startActivity(new Intent(TravelLogContent.this, NextToKinActivity.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

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
