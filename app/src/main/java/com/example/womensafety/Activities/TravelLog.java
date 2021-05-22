package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.womensafety.User.Detail_Forms;
import com.example.womensafety.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class TravelLog extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView Username;

    View hView;
    String verificationCode;

    TextView etvehicleNumber, ettravellingTo, ettravellingFrom, etactualTime, etestimatedTime;
    ImageView imgVehicleImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_log);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("user_tracking_details");


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        hView=navigationView.getHeaderView(0);
        //Username=hView.findViewById(R.id.header_username);
        //String user=getIntent().getStringExtra("use");
        //Username.setText(user);

        etactualTime= (TextView)findViewById(R.id.etActualTime);
        etestimatedTime= (TextView)findViewById(R.id.etEstimatedTime);
        ettravellingFrom= (TextView)findViewById(R.id.etTravellingFrom);
        ettravellingTo= (TextView)findViewById(R.id.etTravellingto);
        etvehicleNumber= (TextView)findViewById(R.id.etVehicleNumber);
        imgVehicleImageUri=(ImageView) findViewById(R.id.vehicleImage);


        final String cud= Objects.requireNonNull(auth.getCurrentUser()).getUid();

        //Add for loop here.
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String actualTime= Objects.requireNonNull(snapshot.child(cud).child("actual_time").getValue()).toString();
                String estimatedTime= Objects.requireNonNull(snapshot.child(cud).child("estimated_time").getValue()).toString();
                String travellingFrom= Objects.requireNonNull(snapshot.child(cud).child("travelling_from").getValue()).toString();
                String travellingTo= Objects.requireNonNull(snapshot.child(cud).child("travelling_to").getValue()).toString();
                String vehicleNumber= Objects.requireNonNull(snapshot.child(cud).child("vehicle_number").getValue()).toString();
                String VehicleImageUri= Objects.requireNonNull(snapshot.child(cud).child("Vehicle_image").getValue()).toString();
                verificationCode= Objects.requireNonNull(snapshot.child(cud).child("mUVC").getValue()).toString();
                etactualTime.setText(actualTime);
                etestimatedTime.setText(estimatedTime);
                ettravellingFrom.setText(travellingFrom);
                ettravellingTo.setText(travellingTo);
                etvehicleNumber.setText(vehicleNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_travelLog:
                        break;

                    case R.id.nav_home:
                        startActivity(new Intent(TravelLog.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(TravelLog.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(TravelLog.this, SuspectRegistrationActivity.class));
                        break;
                    case R.id.nav_nextToKin:
                        startActivity(new Intent(TravelLog.this, NextToKinActivity.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

/*    public void openLogDialog(){
        TravelLogDialog travelLogDialog = new TravelLogDialog();
        travelLogDialog.show(getSupportFragmentManager(), "Travel Log Dialog");
    }*/

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
