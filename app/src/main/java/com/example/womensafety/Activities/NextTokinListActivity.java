package com.example.womensafety.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.womensafety.Adapters.kinAdapter;
import com.example.womensafety.Models.kin_registered;
import com.example.womensafety.R;
import com.example.womensafety.User.Detail_Forms;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Objects;


public class NextTokinListActivity extends AppCompatActivity {

    RecyclerView next_to_kin_list;
    Button add_button;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    kinAdapter adapter;

    View hView;
    TextView Username;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_tokin_list);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Next To kin").child(Objects.requireNonNull(auth.getCurrentUser()).getUid());

        next_to_kin_list = (RecyclerView) findViewById(R.id.next_to_kin_list);
        next_to_kin_list.hasFixedSize();
        next_to_kin_list.setLayoutManager(new LinearLayoutManager(this));
        add_button = findViewById(R.id.add_next_to_kin_button);

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        /*hView=navigationView.getHeaderView(0);
        Username=hView.findViewById(R.id.header_username);
        String user=getIntent().getStringExtra("use");
        Username.setText(user);*/
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(NextTokinListActivity.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(NextTokinListActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(NextTokinListActivity.this, SuspectListActivity.class));
                        break;

                    case R.id.nav_nextToKin:
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(NextTokinListActivity.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(NextTokinListActivity.this, SettingsActivity.class));
                        break;

                    case R.id.nav_emergencyContacts:
                        startActivity(new Intent(NextTokinListActivity.this, EmergencyContactListActivity.class));
                        break;

                    case R.id.nav_manageAccount:
                        startActivity(new Intent(NextTokinListActivity.this, ManageActivity.class));
                        break;

                    case R.id.nav_travelLog:
                        startActivity(new Intent(NextTokinListActivity.this, TravelLogContent.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(NextTokinListActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        try {
            add_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(NextTokinListActivity.this, NextToKinActivity.class));
                }
            });
        } catch(NullPointerException ignored) {

        }
        /*
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NextTokinListActivity.this, NextToKinActivity.class));
            }
        });*/

        final ArrayList<kin_registered> kin = new ArrayList<kin_registered>();

        adapter=new kinAdapter(NextTokinListActivity.this,kin);

        next_to_kin_list.setAdapter(adapter);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot kinSnap : snapshot.getChildren()) {
                    kin_registered kinRegistered = kinSnap.getValue(kin_registered.class);
                    kin.add(kinRegistered);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            /*super.onBackPressed();*/
            startActivity(new Intent(getApplicationContext(),AdminActivity.class));
        }
    }

    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();
    }
}








/*
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
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.womensafety.Adapters.kinAdapter;
import com.example.womensafety.User.Detail_Forms;
import com.example.womensafety.Models.kin_registered;
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
import java.util.Objects;

public class NextTokinListActivity extends AppCompatActivity {

    RecyclerView next_to_kin_list;
    Button add_button;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    kinAdapter adapter;

    View hView;
    TextView Username;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_tokin_list);

        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Next To kin").child(Objects.requireNonNull(auth.getCurrentUser()).getUid());

        next_to_kin_list = (RecyclerView) findViewById(R.id.next_to_kin_list);
        next_to_kin_list.hasFixedSize();
        next_to_kin_list.setLayoutManager(new LinearLayoutManager(this));
        add_button = findViewById(R.id.add_next_to_kin_button);

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(NextTokinListActivity.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(NextTokinListActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(NextTokinListActivity.this, SuspectListActivity.class));
                        break;

                    case R.id.nav_nextToKin:
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(NextTokinListActivity.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(NextTokinListActivity.this, SettingsActivity.class));
                        break;

                    case R.id.nav_emergencyContacts:
                        startActivity(new Intent(NextTokinListActivity.this, EmergencyContactListActivity.class));
                        break;

                    case R.id.nav_travelLog:
                        startActivity(new Intent(NextTokinListActivity.this, TravelLog.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(NextTokinListActivity.this, LoginActivity.class));
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
                startActivity(new Intent(NextTokinListActivity.this, NextToKinActivity.class));
            }
        });

        final ArrayList<kin_registered> kin = new ArrayList<kin_registered>();

        adapter=new kinAdapter(NextTokinListActivity.this,kin);

        next_to_kin_list.setAdapter(adapter);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot kinSnap : snapshot.getChildren()) {
                    kin_registered kinRegistered = kinSnap.getValue(kin_registered.class);
                    kin.add(kinRegistered);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();
            startActivity(new Intent(getApplicationContext(),AdminActivity.class));
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
*/