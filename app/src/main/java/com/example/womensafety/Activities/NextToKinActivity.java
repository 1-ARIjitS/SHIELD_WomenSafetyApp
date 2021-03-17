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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.womensafety.Detail_Forms;
import com.example.womensafety.R;
import com.example.womensafety.Models.kin_registered;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NextToKinActivity extends AppCompatActivity {

    EditText kin_name;
    EditText kin_phone;
    Button kin_add;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_to_kin);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Next To kin").child(Objects.requireNonNull(auth.getCurrentUser()).getUid());

        kin_name = findViewById(R.id.kin_name);
        kin_phone = findViewById(R.id.kin_mobile);
        kin_add = findViewById(R.id.kin_add_button);

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(NextToKinActivity.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(NextToKinActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(NextToKinActivity.this, SuspectRegistrationActivity.class));
                        break;

                    case R.id.nav_nextToKin:
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(NextToKinActivity.this, AboutUsActivity.class));
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(NextToKinActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        kin_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k_name = kin_name.getText().toString();
                String k_phone = kin_phone.getText().toString();
                int c = 0;
                kin_registered kin = new kin_registered(k_name, k_phone);
                if (c >= 0 && c <= 4) {
                    if (k_name.isEmpty() && k_phone.isEmpty()) {
                        Toast.makeText(NextToKinActivity.this, "INVALID,Blank Field", Toast.LENGTH_SHORT).show();
                    } else if (k_name.isEmpty()) {
                        Toast.makeText(NextToKinActivity.this, "INVALID,please enter the name of next to kin", Toast.LENGTH_SHORT).show();
                    } else if (k_phone.isEmpty()) {
                        Toast.makeText(NextToKinActivity.this, "INVALID,please enter contact details of next to kin", Toast.LENGTH_SHORT).show();
                    }else if (k_phone.length()<10) {
                        Toast.makeText(NextToKinActivity.this, "INVALID,mobile number entered is too short", Toast.LENGTH_SHORT).show();
                    }else if (k_phone.length()>10) {
                        Toast.makeText(NextToKinActivity.this, "INVALID,mobile number entered is too long", Toast.LENGTH_SHORT).show();
                    } else {
                        reference.child(k_phone).setValue(kin);
                        Toast.makeText(NextToKinActivity.this, "next to kin successfully added", Toast.LENGTH_SHORT).show();
                        c = c + 1;
                        startActivity(new Intent(NextToKinActivity.this, NextTokinListActivity.class));
                    }
                } else {
                    Toast.makeText(NextToKinActivity.this, "you have already added 5 next to kin contacts to your profile", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(NextToKinActivity.this, NextTokinListActivity.class));
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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
    }
}