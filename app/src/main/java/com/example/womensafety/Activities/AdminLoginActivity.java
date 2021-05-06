package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.womensafety.R;
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

public class AdminLoginActivity extends AppCompatActivity {
    EditText username;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;
    EditText password;

    Button login;

    String email, pass,mUser,mPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("registered_admins");

        username = (EditText) findViewById(R.id.login_username);

        password = (EditText) findViewById(R.id.login_password);
        final String cud="7008945157";
        login = (Button) findViewById(R.id.login_button);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                email= Objects.requireNonNull(snapshot.child(cud).child("r_email").getValue()).toString();
                pass= Objects.requireNonNull(snapshot.child(cud).child("r_password").getValue()).toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AdminLoginActivity.this, email+pass, Toast.LENGTH_SHORT).show();

                mUser = username.getText().toString();

                mPass = password.getText().toString();
                if (mUser.isEmpty() && mPass.isEmpty()) {
                    Toast.makeText(AdminLoginActivity.this, "Invalid,Blank Field", Toast.LENGTH_SHORT).show();
                } else if (mUser.isEmpty()) {
                    Toast.makeText(AdminLoginActivity.this, "Invalid,please enter an Email Id", Toast.LENGTH_SHORT).show();
                } else if (mPass.isEmpty()) {
                    Toast.makeText(AdminLoginActivity.this, "Invalid,please enter a Password", Toast.LENGTH_SHORT).show();
                } else {
                    if(mUser.equals(email) && mPass.equals(pass)){
                                startActivity(new Intent(AdminLoginActivity.this, AdminPanel.class));
                                Toast.makeText(AdminLoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(AdminLoginActivity.this, "Invalid Login Credential", Toast.LENGTH_SHORT).show();

                            }
                        }
                    };
                });
            }
}