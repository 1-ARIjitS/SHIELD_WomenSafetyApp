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

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button create_an_account_button;

    EditText username;

    EditText password;

    FirebaseAuth auth;

    Button login;

    String mUser, mPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        create_an_account_button = (Button) findViewById(R.id.login_create_a_new_account);

        username = (EditText) findViewById(R.id.login_username);

        password = (EditText) findViewById(R.id.login_password);

        login = (Button) findViewById(R.id.login_button);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mUser = username.getText().toString();

                mPass = password.getText().toString();
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
                                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
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
}