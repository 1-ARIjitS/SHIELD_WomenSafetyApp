package com.example.womensafety.Admin.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.womensafety.Activities.LoginActivity;
import com.example.womensafety.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ManageSuperAdminAccountActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView name,age,email,mobile;

    Button change_password;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference mobReference;

    String m_name,m_age,m_email,m_mobile;
    FirebaseUser current_user;
    String current_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_super_admin_account);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_super_admins");

        name=(TextView)findViewById(R.id.manage_full_name);
        age=(TextView)findViewById(R.id.manage_age);
        email=(TextView)findViewById(R.id.manage_email);
        mobile=(TextView)findViewById(R.id.manage_mobile);

        change_password=(Button)findViewById(R.id.manage_password);

        current_user=auth.getCurrentUser();
        current_email=auth.getCurrentUser().getEmail();

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.superadmin_homepage:
                        startActivity(new Intent(ManageSuperAdminAccountActivity.this, SuperAdminHomepage.class));
                        break;

                    case R.id.superadmin_home:
                        startActivity(new Intent(ManageSuperAdminAccountActivity.this, SuperAdminDashboardActivity.class));
                        break;
                    case R.id.superadmin_manage_account:
                        break;

                    case R.id.superadmin_manage_admin:
                        startActivity( new Intent(ManageSuperAdminAccountActivity.this, ManageAdminActivity.class));
                        break;

                    case R.id.superadmin_manage_users:
                        startActivity(new Intent(ManageSuperAdminAccountActivity.this, AdminUserActivity.class));
                        break;

                    case R.id.superadmin_manage_superadmin:
                        startActivity(new Intent(ManageSuperAdminAccountActivity.this, ManageSuperAdminActivity.class));
                        break;

                    case R.id.superadmin_settings:
                        startActivity(new Intent(ManageSuperAdminAccountActivity.this, AdminSettingsActivity.class));
                        break;
                    case R.id.superadmin_logout:
                        auth.signOut();
                        startActivity(new Intent(ManageSuperAdminAccountActivity.this, LoginActivity.class));
                        finish();
                        break;
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot superSnap: snapshot.getChildren())
                {
                    if(superSnap.child("s_email").getValue().toString().equals(current_email))
                    {
                        m_name=superSnap.child("s_name").getValue().toString();
                        m_age=superSnap.child("s_age").getValue().toString();
                        m_mobile=superSnap.child("s_mob_num").getValue().toString();
                        m_email=superSnap.child("s_email").getValue().toString();
                        mobReference=database.getReference("registered_super_admins").child(m_mobile);
                        name.setText(m_name);
                        age.setText(m_age);
                        mobile.setText(m_mobile);
                        email.setText(m_email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassword=new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog=new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("ARE YOU SURE,YOU WANT TO RESET YOUR PASSWORD?");
                passwordResetDialog.setMessage("Enter the new password");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String newPass = resetPassword.getText().toString();
                        current_user.updatePassword(newPass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(),"your password is reset successfully",Toast.LENGTH_SHORT).show();
                                mobReference.child("s_password").setValue(newPass);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(),"password reset failed",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //close
                    }
                });

                passwordResetDialog.create().show();

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