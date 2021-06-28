package com.example.womensafety.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.womensafety.User.Detail_Forms;
import com.example.womensafety.R;
import com.example.womensafety.Models.suspect_registered;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ValueEventListener;

public class SuspectRegistrationActivity extends AppCompatActivity {

    EditText suspect_name, suspect_description, suspect_mobile, suspect_identity;
    Button suspect_register;
    FirebaseAuth auth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    DatabaseReference userReference;

    View hView;
    TextView Username;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    String currentUserId;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspect_registration);

        auth = FirebaseAuth.getInstance();
        currentUserId=auth.getCurrentUser().getUid();



        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("suspects_registered");
        userReference=rootNode.getReference("registered_users");

        suspect_name = findViewById(R.id.suspect_name);
        suspect_description = findViewById(R.id.suspect_description);
        suspect_mobile = findViewById(R.id.suspect_phone_number);
        suspect_identity = findViewById(R.id.suspect_specific_identity);
        suspect_register = findViewById(R.id.button);


        setUpToolbar();
        navigationView=findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(SuspectRegistrationActivity.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(SuspectRegistrationActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        break;

                    case R.id.nav_nextToKin:
                        startActivity(new Intent(SuspectRegistrationActivity.this, NextTokinListActivity.class));
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(SuspectRegistrationActivity.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_emergencyContacts:
                        startActivity(new Intent(SuspectRegistrationActivity.this, EmergencyContactListActivity.class));
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(SuspectRegistrationActivity.this, SettingsActivity.class));
                        break;

                    case R.id.nav_manageAccount:
                        startActivity(new Intent(SuspectRegistrationActivity.this, ManageActivity.class));
                        break;

                    case R.id.nav_travelLog:
                        startActivity(new Intent(SuspectRegistrationActivity.this, TravelLogContent.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(SuspectRegistrationActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    phone=snapshot.child(currentUserId).child("mobile_number").getValue().toString();
                } catch(NullPointerException ignored) {

                }
                //phone=snapshot.child(currentUserId).child("mobile_number").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        suspect_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_name = suspect_name.getText().toString();
                String s_description = suspect_description.getText().toString();
                String s_identity = suspect_identity.getText().toString();
                String s_mobile_num = suspect_mobile.getText().toString();

                suspect_registered sus = new suspect_registered(s_name, s_description, s_identity, s_mobile_num);
                if (s_name.isEmpty() && s_description.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "INVALID,Blank Field", Toast.LENGTH_SHORT).show();
                } else if (s_name.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "INVALID,please enter the name of the suspect", Toast.LENGTH_SHORT).show();
                } else if (s_description.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "INVALID,please enter a valid description of the suspect", Toast.LENGTH_SHORT).show();
                }if (s_mobile_num.length()>0 && s_mobile_num.length() < 10) {
                    Toast.makeText(getApplicationContext(), "INVALID,mobile number is too short ", Toast.LENGTH_SHORT).show();
                }if (s_mobile_num.length() > 10) {
                    Toast.makeText(getApplicationContext(), "INVALID,mobile number is too long", Toast.LENGTH_SHORT).show();
                } else {
                    reference.child(phone).child(s_description).setValue(sus);
                    Toast.makeText(getApplicationContext(), "suspect registration successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SuspectRegistrationActivity.this, SuspectListActivity.class));
                }
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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textColor));
        actionBarDrawerToggle.syncState();
    }

}
