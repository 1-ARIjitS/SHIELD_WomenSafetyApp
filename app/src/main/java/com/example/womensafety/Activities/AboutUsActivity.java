package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.womensafety.Detail_Forms;
import com.example.womensafety.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AboutUsActivity extends AppCompatActivity {

    ImageView linked_in;
    ImageView instagram;
    ImageView facebook;

    TextView mobile_num;
    TextView email_1;
    TextView email_2;
    TextView website;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    View hView;

    TextView Username;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        auth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_users");

        linked_in = findViewById(R.id.linkedin);
        instagram = findViewById(R.id.instagram);
        facebook = findViewById(R.id.facebook);

        website = findViewById(R.id.website);
        mobile_num = findViewById(R.id.about_us_phone);
        email_1 = findViewById(R.id.email_link1);
        email_2 = findViewById(R.id.email_link2);


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
/*        hView=navigationView.getHeaderView(0);

        final String cud= Objects.requireNonNull(auth.getCurrentUser()).getUid();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user= Objects.requireNonNull(snapshot.child(cud).child("full_name").getValue()).toString();
                Username.setText(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
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

                    case R.id.nav_nextToKin:
                        startActivity(new Intent(AboutUsActivity.this, NextTokinListActivity.class));
                        break;

                    case R.id.nav_aboutUs:
                        break;

/*                    case R.id.nav_travelLog:
                        startActivity(new Intent(AboutUsActivity.this, TravelLogContent.class));
                        break;*/

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

        mobile_num.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:7774050060"));

                if (ActivityCompat.checkSelfPermission(AboutUsActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);
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