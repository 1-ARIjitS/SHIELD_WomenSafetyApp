package com.example.womensafety.Activities;

import android.content.Intent;
import android.os.Build;
import android.view.MenuItem;

import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import com.example.womensafety.Adapters.EmergencyContactsAdapter;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.EmergencyContacts;
import com.example.womensafety.User.Detail_Forms;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmergencyContactListActivity extends AppCompatActivity {

    RecyclerView contact_list;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    EmergencyContactsAdapter adapter;

    /*SwitchCompat dark_mode_switch;*/
    Button dark_mode_switch;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact_list);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Default_Emergency_Contacts");

        contact_list=(RecyclerView) findViewById(R.id.emergency_contact_list);
        contact_list.hasFixedSize();
        contact_list.setLayoutManager(new LinearLayoutManager(this));

        final ArrayList<EmergencyContacts>contactsArrayList=new ArrayList<EmergencyContacts>();

        adapter=new EmergencyContactsAdapter(EmergencyContactListActivity.this,contactsArrayList);

        contact_list.setAdapter(adapter);


        try {
            setUpToolbar();
            //navigationView = findViewById(R.id.navigationMenu);
        } catch(RuntimeException ignored) {

        }

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
                        startActivity(new Intent(EmergencyContactListActivity.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(EmergencyContactListActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(EmergencyContactListActivity.this, SuspectListActivity.class));
                        break;

                    case R.id.nav_nextToKin:
                        startActivity(new Intent(EmergencyContactListActivity.this, NextTokinListActivity.class));
                        break;

                        /*
                    case R.id.nav_manageAccount:
                        startActivity(new Intent(EmergencyContactListActivity.this, ManageActivity.class));
                        break;


                         */
                    case R.id.nav_aboutUs:
                        startActivity(new Intent(EmergencyContactListActivity.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(EmergencyContactListActivity.this, SettingsActivity.class));
                        break;

                    case R.id.nav_emergencyContacts:
                        //startActivity(new Intent(EmergencyContactListActivity.this, EmergencyContactListActivity.class));
                        break;

                    case R.id.nav_travelLog:
                        startActivity(new Intent(EmergencyContactListActivity.this, TravelLogContent.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(EmergencyContactListActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });




        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot conSnap : snapshot.getChildren())
                {
                    EmergencyContacts contacts=conSnap.getValue(EmergencyContacts.class);
                    contactsArrayList.add(contacts);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dark_mode_switch=(Button) findViewById(R.id.dar);

        dark_mode_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                }
                else{
                    if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
                    {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    }
                    else
                    {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    }
                }}
        });
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
        try {
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textColor));
        } catch(NullPointerException ignored) {

        }
        //drawerLayout.addDrawerListener(actionBarDrawerToggle);
        //actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();
    }
}












/*
package com.example.womensafety.Activities;

import android.content.Intent;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;

import com.example.womensafety.Adapters.EmergencyContactsAdapter;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.EmergencyContacts;
import com.example.womensafety.User.Detail_Forms;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmergencyContactListActivity extends AppCompatActivity {

    RecyclerView contact_list;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    EmergencyContactsAdapter adapter;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_contact_list);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Default_Emergency_Contacts");

        contact_list=(RecyclerView) findViewById(R.id.emergency_contact_list);
        contact_list.hasFixedSize();
        contact_list.setLayoutManager(new LinearLayoutManager(this));

        final ArrayList<EmergencyContacts>contactsArrayList=new ArrayList<EmergencyContacts>();

        adapter=new EmergencyContactsAdapter(EmergencyContactListActivity.this,contactsArrayList);

        contact_list.setAdapter(adapter);


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(EmergencyContactListActivity.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(EmergencyContactListActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(EmergencyContactListActivity.this, SuspectListActivity.class));
                        break;

                    case R.id.nav_nextToKin:
                        startActivity(new Intent(EmergencyContactListActivity.this, NextTokinListActivity.class));
                        break;

                    case R.id.nav_manageAccount:
                        startActivity(new Intent(EmergencyContactListActivity.this, ManageActivity.class));
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(EmergencyContactListActivity.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(EmergencyContactListActivity.this, SettingsActivity.class));
                        break;

                    case R.id.nav_emergencyContacts:
                        //startActivity(new Intent(EmergencyContactListActivity.this, EmergencyContactListActivity.class));
                        break;

                    case R.id.nav_travelLog:
                        startActivity(new Intent(EmergencyContactListActivity.this, TravelLogContent.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(EmergencyContactListActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 for(DataSnapshot conSnap : snapshot.getChildren())
                 {
                     EmergencyContacts contacts=conSnap.getValue(EmergencyContacts.class);
                     contactsArrayList.add(contacts);
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
            super.onBackPressed();
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
*/