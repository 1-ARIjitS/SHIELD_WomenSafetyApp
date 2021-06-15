package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.womensafety.Adapters.travelLogAdapter;
import com.example.womensafety.Models.suspect_registered;
import com.example.womensafety.Models.userLocationTracking;
import com.example.womensafety.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.womensafety.User.Detail_Forms;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TravelLogContent extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    FirebaseAuth auth;
    String cud;

    RecyclerView TravelLogRecycler;
    ArrayList<userLocationTracking> travelLogList;
    FirebaseDatabase database;
    DatabaseReference reference;
    travelLogAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_log_content);

        auth=FirebaseAuth.getInstance();
        cud= Objects.requireNonNull(auth.getCurrentUser()).getUid();

        database=FirebaseDatabase.getInstance();
        reference=database.getReference("user_tracking_details").child(cud);


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

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

                    case R.id.nav_manageAccount:
                        startActivity(new Intent(TravelLogContent.this, ManageActivity.class));
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(TravelLogContent.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_emergencyContacts:
                        startActivity(new Intent(TravelLogContent.this, EmergencyContactListActivity.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(TravelLogContent.this, LoginActivity.class));
                        finish();
                        break;

                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        //recycler view operations starting here

        TravelLogRecycler=findViewById(R.id.travel_log_recycler);

        TravelLogRecycler.setLayoutManager(new LinearLayoutManager(this));

        TravelLogRecycler.hasFixedSize();

        travelLogList=new ArrayList<userLocationTracking>();

        adapter=new travelLogAdapter(TravelLogContent.this,travelLogList);

        TravelLogRecycler.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dateSnap : snapshot.getChildren()) {
                    for (DataSnapshot timeSnap : dateSnap.getChildren()) {
                        userLocationTracking userTravelLog = timeSnap.getValue(userLocationTracking.class);
                        travelLogList.add(userTravelLog);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textColor));
        actionBarDrawerToggle.syncState();
    }
}
