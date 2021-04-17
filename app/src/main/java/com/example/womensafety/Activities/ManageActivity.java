package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.womensafety.Detail_Forms;
import com.example.womensafety.Models.kin_registered;
import com.example.womensafety.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.concurrent.Future;

public class ManageActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    TextView Username;
    TextView etUserName;
    TextView etFullName;
    TextView etEmail;
    TextView etAge;
    TextView etMobile;
    View hView;
    Button btnChangePass;
    Button btnChangeVerificationCode;
    String password;
    String verificationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("registered_users");


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        hView=navigationView.getHeaderView(0);
        Username=hView.findViewById(R.id.header_username);
        String user=getIntent().getStringExtra("use");
        Username.setText(user);

        etUserName= (TextView)findViewById(R.id.etUserName);
        etFullName = (TextView)findViewById(R.id.etFullName);
        etEmail= (TextView)findViewById(R.id.etEmail);
        etAge= (TextView)findViewById(R.id.etAge);
        etMobile= (TextView)findViewById(R.id.etMobile);
        btnChangePass=(Button)findViewById(R.id.btnChangePass);
        btnChangeVerificationCode=(Button)findViewById(R.id.btnChangeVerificationCode);

        final String cud= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name= Objects.requireNonNull(snapshot.child(cud).child("full_name").getValue()).toString();
                String email= Objects.requireNonNull(snapshot.child(cud).child("mEmail_id").getValue()).toString();
                String age= Objects.requireNonNull(snapshot.child(cud).child("mAge").getValue()).toString();
                String mobile= Objects.requireNonNull(snapshot.child(cud).child("mMobile_number").getValue()).toString();
                password= Objects.requireNonNull(snapshot.child(cud).child("mPassword").getValue()).toString();
                verificationCode= Objects.requireNonNull(snapshot.child(cud).child("mUVC").getValue()).toString();
                etUserName.setText(name);
                etFullName.setText(name);
                etEmail.setText(email);
                etAge.setText(age);
                etMobile.setText(mobile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        btnChangePass.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(ManageActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.custom_dialog,null);
                final EditText txtOld = (EditText)mView.findViewById(R.id.txt_old);
                final EditText txtNew = (EditText)mView.findViewById(R.id.txt_new);
                final EditText txtNew2 = (EditText)mView.findViewById(R.id.txt_new2);
                Button btn_cancel = (Button)mView.findViewById(R.id.btn_cancel);
                Button btn_change = (Button)mView.findViewById(R.id.btn_change);
                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String old=txtOld.getText().toString();
                        final String newPass =txtNew.getText().toString();
                        String confirmPass=txtNew2.getText().toString();

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        //if(TextUtils.isEmpty(old) && newPass!= null && confirmPass!= null){
                        if(old.equals(password)){
                            if(newPass.equals(confirmPass)) {
                                user.updatePassword(newPass)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext(), "Password changed Successfully", Toast.LENGTH_SHORT).show();
                                                    reference.child(cud).child("mPassword").setValue(newPass);
                                                    alertDialog.dismiss();

                                                } else {
                                                    Toast.makeText(getApplicationContext(), "Some error occurred! Please try after some time", Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });
                            }else{
                                Toast.makeText(getApplicationContext(), "New Password do not match", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Old Password is incorrect", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                alertDialog.show();
            }


        });

        btnChangeVerificationCode.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alert = new AlertDialog.Builder(ManageActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.custom_dialog,null);
                final EditText txtOld = (EditText)mView.findViewById(R.id.txt_old);
                final EditText txtNew = (EditText)mView.findViewById(R.id.txt_new);
                final EditText txtNew2 = (EditText)mView.findViewById(R.id.txt_new2);
                Button btn_cancel = (Button)mView.findViewById(R.id.btn_cancel);
                Button btn_change = (Button)mView.findViewById(R.id.btn_change);
                alert.setView(mView);
                final AlertDialog alertDialog = alert.create();
                alertDialog.setCanceledOnTouchOutside(false);
                btn_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                btn_change.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String old=txtOld.getText().toString();
                        String newPass =txtNew.getText().toString();
                        String confirmPass=txtNew2.getText().toString();
                        if(old.equals(verificationCode)) {
                            if (newPass.equals(confirmPass)) {
                                reference.child(cud).child("mUVC").setValue(newPass);
                                Toast.makeText(getApplicationContext(), "Password changed Successfully", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            } else {
                                Toast.makeText(getApplicationContext(), "New Password do not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Old Password is incorrect", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
                alertDialog.show();
            }


        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(ManageActivity.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(ManageActivity.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(ManageActivity.this, SuspectListActivity.class));
                        break;

                    case R.id.next_to_kin_list:
                        startActivity(new Intent(ManageActivity.this, NextToKinActivity.class));
                        break;

                    case R.id.nav_manageAccount:
                        break;

                    case R.id.nav_aboutUs:
                        startActivity(new Intent(ManageActivity.this, AboutUsActivity.class));
                        break;


                    case R.id.nav_logout:
                        auth.signOut();
                        startActivity(new Intent(ManageActivity.this, LoginActivity.class));
                        finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
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