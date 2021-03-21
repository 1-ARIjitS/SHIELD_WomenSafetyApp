package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.womensafety.Detail_Forms;
import com.example.womensafety.Models.users;
import com.example.womensafety.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageAccount extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;


      FirebaseUser user;
      DatabaseReference fRef = FirebaseDatabase.getInstance().getReference("registered_users");
      String uid;

    TextView tv_UserName, tv_UserAge;

    Button bt_changeLoginPass, bt_changeVerificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_account);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        tv_UserName = findViewById(R.id.disp_userName);
        tv_UserAge = findViewById(R.id.disp_userAge);

        bt_changeLoginPass = findViewById(R.id.bt_changeLoginPass);
        bt_changeVerificationCode = findViewById(R.id.bt_changeUniqueVerificationCode);

        fRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String userName = snapshot.child(uid).child("mFull_name").getValue(String.class);
                String userAge = snapshot.child(uid).child("mAge").getValue(String.class);

                tv_UserName.setText(userName+"");
                tv_UserAge.setText(userAge+"");
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
                        startActivity(new Intent(ManageAccount.this, TravelLog.class));
                        break;

                    case R.id.nav_home:
                        startActivity(new Intent(ManageAccount.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(ManageAccount.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(ManageAccount.this, SuspectRegistrationActivity.class));
                        break;
                    case R.id.nav_nextToKin:
                        startActivity(new Intent(ManageAccount.this, NextToKinActivity.class));
                        break;

                    case R.id.nav_manageAccount:
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
    }
}
