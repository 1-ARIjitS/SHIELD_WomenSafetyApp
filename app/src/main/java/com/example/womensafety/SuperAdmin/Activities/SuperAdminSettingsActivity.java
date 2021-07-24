package com.example.womensafety.SuperAdmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.Activities.SelectUserActivity;
import com.example.womensafety.Activities.SuspectListActivity;
import com.example.womensafety.Admin.Activities.AdminHomepageActivity;
import com.example.womensafety.Admin.Activities.AdminUserActivity;
import com.example.womensafety.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SuperAdminSettingsActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    FirebaseAuth auth;

    String flag="0";

    FloatingActionButton dark_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_settings);

        auth=FirebaseAuth.getInstance();
        try {
            flag = getIntent().getStringExtra("flag");
        }
        catch (Exception ignored) {
        }

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        /*if(flag.equals("1")){
            Menu menu = navigationView.getMenu();
            // find MenuItem you want to change
            menu.removeItem(R.id.superadmin_home);
            menu.removeItem(R.id.superadmin_manage_superadmin);
            menu.removeItem(R.id.superadmin_manage_account);
            menu.removeItem(R.id.superadmin_manage_admin);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.superadmin_homepage:
                            startActivity(new Intent(SuperAdminSettingsActivity.this, AdminHomepageActivity.class));
                            break;
                        case R.id.superadmin_manage_users:
                            startActivity(new Intent(SuperAdminSettingsActivity.this, AdminUserActivity.class));
                            break;
                        case R.id.superadmin_settings:
                            break;
                        case R.id.superadmin_logout:
                            auth.signOut();
                            startActivity(new Intent(SuperAdminSettingsActivity.this, LoginActivity.class));
                            finish();
                            break;
                    }
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        }*/
        /*else{*/

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.superadmin_homepage:
                        startActivity(new Intent(SuperAdminSettingsActivity.this, SuperAdminHomepage.class));
                        break;

                    case R.id.superadmin_home:
                        startActivity(new Intent(SuperAdminSettingsActivity.this, SuperAdminDashboardActivity.class));
                        break;
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(SuperAdminSettingsActivity.this, ManageSuperAdminAccountActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        startActivity(new Intent(SuperAdminSettingsActivity.this, ManageAdminActivity.class));
                        break;

                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(SuperAdminSettingsActivity.this, SuperAdminUsersActivity.class));
                        break;

                    case R.id.superadmin_manage_superadmin:
                        startActivity(new Intent(SuperAdminSettingsActivity.this, ManageSuperAdminActivity.class));
                        break;

                    case R.id.superadmin_settings:
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(SuperAdminSettingsActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });/*}*/


        dark_switch=(FloatingActionButton) findViewById(R.id.dark_action);

        dark_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
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












/*
package com.example.womensafety.SuperAdmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.Activities.SelectUserActivity;
import com.example.womensafety.Activities.SuspectListActivity;
import com.example.womensafety.Admin.Activities.AdminHomepageActivity;
import com.example.womensafety.Admin.Activities.AdminUserActivity;
import com.example.womensafety.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class SuperAdminSettingsActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    FirebaseAuth auth;

    String flag="0";

    FloatingActionButton dark_switch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_super_admin_settings);

        auth=FirebaseAuth.getInstance();
        try {
            flag = getIntent().getStringExtra("flag");
        }
        catch (Exception ignored) {
        }

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        if(flag.equals("1")){
            Menu menu = navigationView.getMenu();

            // find MenuItem you want to change
            menu.removeItem(R.id.superadmin_home);
            menu.removeItem(R.id.superadmin_manage_superadmin);
            menu.removeItem(R.id.superadmin_manage_account);
            menu.removeItem(R.id.superadmin_manage_admin);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {

                        case R.id.superadmin_homepage:
                            startActivity(new Intent(SuperAdminSettingsActivity.this, AdminHomepageActivity.class));
                            break;

                        case R.id.superadmin_manage_users:
                            startActivity(new Intent(SuperAdminSettingsActivity.this, AdminUserActivity.class));
                            break;

                        case R.id.superadmin_settings:
                            break;
                        case R.id.superadmin_logout:
                            auth.signOut();
                            startActivity(new Intent(SuperAdminSettingsActivity.this, LoginActivity.class));
                            finish();
                            break;

                    }

                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
            });
        }
        else{

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.superadmin_homepage:
                        startActivity(new Intent(SuperAdminSettingsActivity.this, SuperAdminHomepage.class));
                        break;

                    case R.id.superadmin_home:
                        startActivity(new Intent(SuperAdminSettingsActivity.this, SuperAdminDashboardActivity.class));
                        break;
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(SuperAdminSettingsActivity.this, ManageSuperAdminAccountActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        startActivity(new Intent(SuperAdminSettingsActivity.this, ManageAdminActivity.class));
                        break;

                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(SuperAdminSettingsActivity.this, SuperAdminUsersActivity.class));
                        break;

                    case R.id.superadmin_manage_superadmin:
                        startActivity(new Intent(SuperAdminSettingsActivity.this, ManageSuperAdminActivity.class));
                        break;

                    case R.id.superadmin_settings:
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(SuperAdminSettingsActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });}


        dark_switch=(FloatingActionButton) findViewById(R.id.dark_action);

        dark_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppCompatDelegate.getDefaultNightMode()==AppCompatDelegate.MODE_NIGHT_YES)
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.black));
        actionBarDrawerToggle.syncState();
    }
}
*/