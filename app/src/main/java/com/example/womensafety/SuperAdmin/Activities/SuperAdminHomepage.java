package com.example.womensafety.SuperAdmin.Activities;

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
import android.widget.TextView;

import com.example.womensafety.Activities.SelectUserActivity;
import com.example.womensafety.Activities.SettingsActivity;
import com.example.womensafety.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SuperAdminHomepage extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    FirebaseAuth auth;

    Button feeds,manage_super,users,manage_admins,manage_account,settings;

    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_homepage);

        total=(TextView)findViewById(R.id.home_users_reg);


        feeds=(Button)findViewById(R.id.home_feeds);
        manage_super=(Button)findViewById(R.id.home_manage_superadmin);
        manage_admins=(Button)findViewById(R.id.home_manage_admin);
        users=(Button)findViewById(R.id.home_users);
        settings=(Button)findViewById(R.id.home_settings);
        manage_account=(Button)findViewById(R.id.home_manage_account);

        auth=FirebaseAuth.getInstance();

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.superadmin_home:
                        startActivity(new Intent(SuperAdminHomepage.this, SuperAdminDashboardActivity.class));
                        break;
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(SuperAdminHomepage.this, SelectUserActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        startActivity(new Intent(SuperAdminHomepage.this, ManageAdminActivity.class));
                        break;

                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(SuperAdminHomepage.this, SuperAdminUsersActivity.class));
                        break;

                    case R.id.superadmin_settings:
                        startActivity(new Intent(SuperAdminHomepage.this, SuperAdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(SuperAdminHomepage.this, SelectUserActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        feeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuperAdminHomepage.this, SuperAdminDashboardActivity.class));
            }
        });
        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuperAdminHomepage.this, SuperAdminUsersActivity.class));
            }
        });
        manage_admins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuperAdminHomepage.this, ManageAdminActivity.class));
            }
        });
        manage_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuperAdminHomepage.this, SelectUserActivity.class));
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuperAdminHomepage.this, SuperAdminSettingsActivity.class));
            }
        });
        manage_super.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SuperAdminHomepage.this, ManageAdminActivity.class));
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