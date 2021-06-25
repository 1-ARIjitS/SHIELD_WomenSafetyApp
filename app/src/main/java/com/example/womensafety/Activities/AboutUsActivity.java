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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.womensafety.User.Detail_Forms;
import com.example.womensafety.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class AboutUsActivity extends AppCompatActivity {

    ImageView linked_in;
    ImageView instagram;
    ImageView facebook;

    TextView mobile_num;
    TextView email_1;
    TextView email_2;
    TextView website;

    FirebaseAuth auth;


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        auth = FirebaseAuth.getInstance();
/*        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_users");*/

        linked_in = findViewById(R.id.linkedin);
        instagram = findViewById(R.id.instagram);
        facebook = findViewById(R.id.facebook);

        website = findViewById(R.id.website);
        mobile_num = findViewById(R.id.about_us_phone);
        email_1 = findViewById(R.id.email_link1);
        email_2 = findViewById(R.id.email_link2);


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(AboutUsActivity.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(AboutUsActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(AboutUsActivity.this, SuspectListActivity.class));
                        break;
                    case R.id.nav_emergencyContacts:
                        startActivity(new Intent(AboutUsActivity.this, EmergencyContactListActivity.class));
                        break;

                    case R.id.nav_nextToKin:
                        startActivity(new Intent(AboutUsActivity.this, NextTokinListActivity.class));
                        break;

                    case R.id.nav_manageAccount:
                        startActivity(new Intent(AboutUsActivity.this, ManageActivity.class));
                        break;

                    case R.id.nav_settings:
                        startActivity(new Intent(AboutUsActivity.this, SettingsActivity.class));
                        break;

                    case R.id.nav_travelLog:
                        startActivity(new Intent(AboutUsActivity.this, TravelLogContent.class));
                        break;

                    case R.id.nav_aboutUs:
                        break;

                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(AboutUsActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        linked_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Url = "https://www.linkedin.com/company/cybertronsoftwares/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Url));
                startActivity(intent);
            }
        });
        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Url = "https://www.instagram.com/cybertronsoftwares/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Url));
                startActivity(intent);
            }
        });
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Url = "https://m.facebook.com/cybertronsoftware/?ti=as";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Url));
                startActivity(intent);
            }
        });

        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Url = "https://www.cybertronsoftwares.com/";
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(Url));
                startActivity(intent);
            }
        });


        email_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + email_1.getText().toString())); // only email apps should handle this
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        email_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + email_2.getText().toString())); // only email apps should handle this
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(getApplicationContext(), AdminActivity.class));
            /* super.onBackPressed();*/
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