package com.example.womensafety.SuperAdmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.Activities.ManageActivity;
import com.example.womensafety.Activities.SelectUserActivity;
import com.example.womensafety.Activities.SettingsActivity;
import com.example.womensafety.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SuperAdminHomepage extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference superAdminReference;

    int num=0;

    Button feeds,manage_super,users,manage_admins,manage_account,settings;

    TextView total,super_admin_username;

    String email,username;

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
        super_admin_username=(TextView)findViewById(R.id.superadmin_homepage_name);

        /*email=getIntent().getStringExtra("emailId");*/
       /* Log.d("email_id",email);*/
        /*final String MAIL=email;*/

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_users");
        superAdminReference=database.getReference("registered_super_admins");

        email=auth.getCurrentUser().getEmail();



        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.superadmin_homepage:
                        break;

                    case R.id.superadmin_home:
                        startActivity(new Intent(SuperAdminHomepage.this, SuperAdminDashboardActivity.class));
                        break;
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(SuperAdminHomepage.this, ManageSuperAdminAccountActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        startActivity(new Intent(SuperAdminHomepage.this, ManageAdminActivity.class));
                        break;

                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(SuperAdminHomepage.this, SuperAdminUsersActivity.class));
                        break;

                    case R.id.superadmin_manage_superadmin:
                        startActivity(new Intent(SuperAdminHomepage.this, ManageSuperAdminActivity.class));
                        break;

                    case R.id.superadmin_settings:
                        startActivity(new Intent(SuperAdminHomepage.this, SuperAdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(SuperAdminHomepage.this, LoginActivity.class));
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
                startActivity(new Intent(SuperAdminHomepage.this, ManageSuperAdminAccountActivity.class));
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
                startActivity(new Intent(SuperAdminHomepage.this, ManageSuperAdminActivity.class));
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                num=(int)snapshot.getChildrenCount();
                total.setText(Integer.toString(num));}
                else{
                    total.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        superAdminReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot superAdmin : snapshot.getChildren())
                {
                    if(superAdmin.child("s_email").getValue().toString().equals(email))
                    {
                       username=superAdmin.child("s_name").getValue().toString();
                       Log.d("name",username);
                       super_admin_username.setText("Welcome "+username);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Log.d("Email",email);
        /*super_admin_username.setText("Welcome "+username);*/
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
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