package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.womensafety.Admin.Activities.AdminHomepageActivity;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Activities.SuperAdminHomepage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button create_an_account_button;
    EditText username;
    EditText password;
    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference adminReference;
    DatabaseReference superAdminReference;
    Button login;
    String mUser, mPass;
    Boolean isAdmin = false;
    Boolean isSuperAdmin = false;
    ProgressBar progress;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        adminReference = database.getReference("registered_admins");
        pref = getSharedPreferences("user_details",MODE_PRIVATE);

        boolean isLogin=pref.getBoolean("isLogin",false);

        if(isLogin){
            Toast.makeText(getApplicationContext(), "User already login!",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(LoginActivity.this, AdminActivity.class));
        }

        superAdminReference = database.getReference("registered_super_admins");

        create_an_account_button = (Button) findViewById(R.id.login_create_a_new_account);

        username = (EditText) findViewById(R.id.login_username);

        password = (EditText) findViewById(R.id.login_password);

        login = (Button) findViewById(R.id.login_button);

        progress = new ProgressBar(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUser = username.getText().toString();

                mPass = password.getText().toString();


                adminReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot admin : snapshot.getChildren()) {
                            if (admin.child("r_email").getValue().toString().equals(mUser)) {
                                isAdmin = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                superAdminReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot superAdmin : snapshot.getChildren()) {
                            if (superAdmin.child("s_email").getValue().toString().equals(mUser)) {
                                isSuperAdmin = true;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                if (mUser.isEmpty() && mPass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Invalid,Blank Field", Toast.LENGTH_SHORT).show();
                } else if (mUser.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Invalid,please enter an Email Id", Toast.LENGTH_SHORT).show();
                } else if (mPass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Invalid,please enter a Password", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(mUser, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (isSuperAdmin) {
                                    Intent intent = new Intent(LoginActivity.this, SuperAdminHomepage.class);
                                    intent.putExtra("emailId", mUser);
                                    startActivity(intent);

                                } else if (isAdmin) {
                                    startActivity(new Intent(LoginActivity.this, AdminHomepageActivity.class));
                                } else {
                                    SharedPreferences.Editor editor = pref.edit();
                                    editor.putBoolean("isLogin",true);
                                    editor.apply();
                                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                }
                                Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        create_an_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}