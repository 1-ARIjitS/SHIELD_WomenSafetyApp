package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.womensafety.Adapters.suspectAdapter;
import com.example.womensafety.User.Detail_Forms;
import com.example.womensafety.Models.suspect_registered;
import com.example.womensafety.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SuspectListActivity extends AppCompatActivity {

    RecyclerView sus_list;
    Button add_button;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    suspectAdapter adapter;

    View hView;
    TextView Username;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspect_list);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        /*
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mob=snapshot.child("mobile_number").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        reference = database.getReference("suspects_registered")/*.child(mob)*/;

        sus_list =(RecyclerView) findViewById(R.id.sus_list);
        sus_list.setLayoutManager(new LinearLayoutManager(this));

        add_button = findViewById(R.id.sus_reg_button);

        final ArrayList<suspect_registered> sus = new ArrayList<>();

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
/*        hView=navigationView.getHeaderView(0);
        Username=hView.findViewById(R.id.header_username);
        String user=getIntent().getStringExtra("use");
        Username.setText(user);*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(SuspectListActivity.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(SuspectListActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        break;

                    case R.id.nav_nextToKin:
                        startActivity(new Intent(SuspectListActivity.this, NextTokinListActivity.class));
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(SuspectListActivity.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(SuspectListActivity.this, SettingsActivity.class));
                        break;

                    case R.id.nav_emergencyContacts:
                        startActivity(new Intent(SuspectListActivity.this, EmergencyContactListActivity.class));
                        break;

                    case R.id.nav_travelLog:
                        startActivity(new Intent(SuspectListActivity.this, TravelLogContent.class));
                        break;

                    case R.id.nav_manageAccount:
                        startActivity(new Intent(SuspectListActivity.this, ManageActivity.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(SuspectListActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuspectListActivity.this, SuspectRegistrationActivity.class));
            }
        });

        adapter = new suspectAdapter(SuspectListActivity.this, sus);

        sus_list.setAdapter(adapter);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot susDesSnap : snapshot.getChildren()) {
                    for (DataSnapshot susSnap : susDesSnap.getChildren()) {
                        suspect_registered suspectRegistered = susSnap.getValue(suspect_registered.class);
                        sus.add(suspectRegistered);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*FirebaseRecyclerOptions<suspect_registered>options=new FirebaseRecyclerOptions.Builder<suspect_registered>()
                .setQuery(reference,suspect_registered.class).build();

        adapter = new suspectAdapter(options);
        sus_list.setAdapter(adapter);*/

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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textColor));
        actionBarDrawerToggle.syncState();
    }
}