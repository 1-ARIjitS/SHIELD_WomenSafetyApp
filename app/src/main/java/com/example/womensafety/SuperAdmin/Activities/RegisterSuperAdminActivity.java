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
import com.example.womensafety.SuperAdmin.Models.SuperAdmins;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Objects;

public class RegisterSuperAdminActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    /*SwitchCompat dark_mode_switch;*/
    Button dark_mode_switch;

    Button create_super_admin_button;
    EditText name,password,age,email,mob_num,address;
    String m_gender;
    String m_name,m_password,m_age,m_email,m_mob_num,m_address;
    MaterialSpinner spinner;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_super_admin);

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
                        startActivity(new Intent(RegisterSuperAdminActivity.this, SuperAdminHomepage.class));
                        break;

                    case R.id.superadmin_home:
                        startActivity(new Intent(RegisterSuperAdminActivity.this, SuperAdminDashboardActivity.class));
                        break;
                    case R.id.superadmin_manage_account:
                        startActivity( new Intent(RegisterSuperAdminActivity.this, ManageSuperAdminAccountActivity.class));
                        break;

                    case R.id.superadmin_manage_admin:
                        break;

                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(RegisterSuperAdminActivity.this, SuperAdminUsersActivity.class));
                        break;

                    case R.id.superadmin_manage_superadmin:
                        startActivity(new Intent(RegisterSuperAdminActivity.this, ManageSuperAdminActivity.class));
                        break;

                    case R.id.superadmin_settings:
                        startActivity(new Intent(RegisterSuperAdminActivity.this, SuperAdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(RegisterSuperAdminActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });



        spinner=(MaterialSpinner)findViewById(R.id.create_superadmin_gender_spinner);
        spinner.setItems("Male","Female");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(item=="Male")
                {
                    m_gender="Male";
                }else
                {
                    m_gender="Female";
                }
            }
        });

        create_super_admin_button=(Button)findViewById(R.id.create_superadmin_button);
        name=(EditText)findViewById(R.id.create_superadmin_name);
        age=(EditText)findViewById(R.id.create_superadmin_age);
        password=(EditText)findViewById(R.id.create_superadmin_password);
        address=(EditText)findViewById(R.id.create_superadmin_address);
        email=(EditText)findViewById(R.id.create_superadmin_email);
        mob_num=(EditText)findViewById(R.id.create_superadmin_mob);

        create_super_admin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_name=name.getText().toString();
                m_age=age.getText().toString();
                m_password=password.getText().toString();
                m_email=email.getText().toString();
                m_address=address.getText().toString();
                m_mob_num=mob_num.getText().toString();

                final SuperAdmins superAdmins=new SuperAdmins(m_name,m_password,m_age,m_email,m_mob_num,m_address,m_gender);

                if (m_name.isEmpty() && m_age.isEmpty() && m_address.isEmpty() && m_email.isEmpty() && m_mob_num.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "INVALID,Blank Fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (m_name.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an username", Toast.LENGTH_SHORT).show();
                    }
                    if (m_address.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an address", Toast.LENGTH_SHORT).show();
                    }
                    if (m_age.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an age", Toast.LENGTH_SHORT).show();
                    }
                    if (m_mob_num.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter a mobile number", Toast.LENGTH_SHORT).show();
                    }
                    if (m_email.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an email id", Toast.LENGTH_SHORT).show();
                    } else if (m_mob_num.length() > 0 && m_mob_num.length() < 10) {
                        Toast.makeText(getApplicationContext(), "INVALID,mobile number is too short", Toast.LENGTH_SHORT).show();
                    } else if (m_mob_num.length() > 10) {
                        Toast.makeText(getApplicationContext(), "INVALID,mobile number is too long", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(m_age) > 130 || Integer.parseInt(m_age) < 0) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter a valid age", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.createUserWithEmailAndPassword(m_email, m_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    reference.child(m_mob_num).setValue(superAdmins);
                                    startActivity(new Intent(getApplicationContext(), ManageSuperAdminActivity.class));
                                    Toast.makeText(getApplicationContext(), "A New SuperAdmin Account Is Successfully Created", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
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