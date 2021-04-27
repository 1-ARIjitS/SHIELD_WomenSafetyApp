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

import com.example.womensafety.Activities.SelectUserActivity;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Adapters.ManageAdminAdapter;
import com.example.womensafety.SuperAdmin.Models.Admins;
import com.example.womensafety.User.Detail_Forms;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManageAdminActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    Button add_button;

    ListView manage_admin_list;

    FirebaseAuth auth;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_admin);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_admins");

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.superadmin_home:
                        startActivity( new Intent(ManageAdminActivity.this, SuperAdminDashboardActivity.class));
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(ManageAdminActivity.this, SelectUserActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        break;

                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(ManageAdminActivity.this, SuperAdminUsersActivity.class));
                        break;
                    case R.id.superadmin_settings:
                        startActivity(new Intent(ManageAdminActivity.this, SuperAdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(ManageAdminActivity.this, SelectUserActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        add_button=(Button)findViewById(R.id.add_admin_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ManageAdminActivity.this,RegisterAdminActivity.class));
            }
        });



        manage_admin_list=(ListView)findViewById(R.id.manage_admins_list);

        final ArrayList<Admins> adminsArrayList=new ArrayList<>();



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot adminSnap : snapshot.getChildren())
                {
                    Admins admins=adminSnap.getValue(Admins.class);
                    adminsArrayList.add(admins);
                }
                ManageAdminAdapter adapter=new ManageAdminAdapter(ManageAdminActivity.this,0,adminsArrayList);

                manage_admin_list.setAdapter(adapter);
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