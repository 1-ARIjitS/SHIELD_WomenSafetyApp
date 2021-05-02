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
import android.widget.ListView;

import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.Activities.SelectUserActivity;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Adapters.ManageSuperAdminAdapter;
import com.example.womensafety.SuperAdmin.Models.SuperAdmins;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageSuperAdminActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    ListView superAdminList;

    Button addButton;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_super_admin);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_super_admins");


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.superadmin_homepage:
                        startActivity( new Intent(ManageSuperAdminActivity.this, SuperAdminHomepage.class));
                        break;

                    case R.id.superadmin_home:
                        startActivity( new Intent(ManageSuperAdminActivity.this, SuperAdminDashboardActivity.class));
                        break;

                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(ManageSuperAdminActivity.this, SelectUserActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        startActivity(new Intent(ManageSuperAdminActivity.this, ManageAdminActivity.class));
                        break;

                    case R.id.superadmin_manage_superadmin:
                        break;

                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(ManageSuperAdminActivity.this, SuperAdminUsersActivity.class));
                        break;
                    case R.id.superadmin_settings:
                        startActivity(new Intent(ManageSuperAdminActivity.this, SuperAdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(ManageSuperAdminActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        addButton=(Button)findViewById(R.id.add_superadmin_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageSuperAdminActivity.this,RegisterSuperAdminActivity.class));
            }
        });

        superAdminList=(ListView)findViewById(R.id.manage_superadmin_list);

        final ArrayList<SuperAdmins> superAdminsArrayList=new ArrayList<SuperAdmins>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot superAdminSnap : snapshot.getChildren())
                {
                    SuperAdmins s_admin=superAdminSnap.getValue(SuperAdmins.class);
                    superAdminsArrayList.add(s_admin);
                }

                ManageSuperAdminAdapter adapter=new ManageSuperAdminAdapter(ManageSuperAdminActivity.this,0,superAdminsArrayList);

                superAdminList.setAdapter(adapter);
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
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
    }
}