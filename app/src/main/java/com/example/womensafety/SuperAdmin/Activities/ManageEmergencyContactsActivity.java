package com.example.womensafety.SuperAdmin.Activities;

import android.os.Build;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.EmergencyContacts;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManageEmergencyContactsActivity extends AppCompatActivity {

    EditText service,mob;
    Button add_contact;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    /*SwitchCompat dark_mode_switch;*/
    Button dark_mode_switch;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_emergency_contacts);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Default_Emergency_Contacts");

        service=(EditText)findViewById(R.id.emergency_name);
        mob=(EditText)findViewById(R.id.emergency_mobile_number);
        add_contact=(Button)findViewById(R.id.emergency_add_button);

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.superadmin_homepage:
                        startActivity(new Intent(ManageEmergencyContactsActivity.this, SuperAdminHomepage.class));
                        break;

                    case R.id.superadmin_home:
                        startActivity(new Intent(ManageEmergencyContactsActivity.this, SuperAdminDashboardActivity.class));
                        break;
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(ManageEmergencyContactsActivity.this, ManageSuperAdminAccountActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        break;

                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(ManageEmergencyContactsActivity.this, SuperAdminUsersActivity.class));
                        break;

                    case R.id.superadmin_manage_superadmin:
                        startActivity(new Intent(ManageEmergencyContactsActivity.this, ManageSuperAdminActivity.class));
                        break;

                    case R.id.superadmin_settings:
                        startActivity(new Intent(ManageEmergencyContactsActivity.this, SuperAdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(ManageEmergencyContactsActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name,mobile;
                name=service.getText().toString();
                mobile=mob.getText().toString();

                EmergencyContacts contacts=new EmergencyContacts(name,mobile);

                if(name.isEmpty()&&mobile.isEmpty())
                {
                    Toast.makeText(ManageEmergencyContactsActivity.this, "INVALID,Blank Field", Toast.LENGTH_SHORT).show();
                }else if(name.isEmpty()){
                    Toast.makeText(ManageEmergencyContactsActivity.this, "INVALID,please enter the name of the emergency service", Toast.LENGTH_SHORT).show();
                }else if(name.isEmpty()){
                    Toast.makeText(ManageEmergencyContactsActivity.this, "INVALID,please enter an emergency contact", Toast.LENGTH_SHORT).show();
                }else{
                    reference.child(mobile).setValue(contacts);
                    Toast.makeText(getApplicationContext(), "Emergency contact successfully saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ManageEmergencyContactsActivity.this,SuperAdminHomepage.class));
                }

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
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textColor));
        actionBarDrawerToggle.syncState();
    }
}