package com.example.womensafety.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.womensafety.R;
import com.google.firebase.auth.FirebaseAuth;

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

        create_an_account_button = findViewById(R.id.login_create_a_new_account);

        username = findViewById(R.id.login_username);

        password = findViewById(R.id.login_password);

        login = findViewById(R.id.login_button);

        login.setOnClickListener(v -> {

            mUser = username.getText().toString();

            mPass = password.getText().toString();
            if (mUser.isEmpty() && mPass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Invalid,Blank Field", Toast.LENGTH_SHORT).show();
            } else if (mUser.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Invalid,please enter an Email Id", Toast.LENGTH_SHORT).show();
            } else if (mPass.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Invalid,please enter a Password", Toast.LENGTH_SHORT).show();
            } else {

                auth.signInWithEmailAndPassword(mUser, mPass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                        Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        create_an_account_button.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        /*SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                // Launch Sign In
                signInToGoogle();
            }
        });
        // Configure Google Client
        configureGoogleClient();*/
    }
}