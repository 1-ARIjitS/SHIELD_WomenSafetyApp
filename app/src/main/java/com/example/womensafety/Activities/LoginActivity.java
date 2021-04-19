package com.example.womensafety.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.womensafety.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    Button create_an_account_button;

    EditText username;

    EditText password;

    FirebaseAuth auth;

    Button login;

    String mUser, mPass;
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 10;
    GoogleSignInClient googleSignInClient;
    private FirebaseAuth firebaseAuth;

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

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                // Launch Sign In
                signInToGoogle();
            }
        });
        // Configure Google Client
        configureGoogleClient();
    }
    private void configureGoogleClient() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                // for the requestIdToken, this is in the values.xml file that
                // is generated from your google-services.json
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        // Set the dimensions of the sign-in button.
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            showToastMessage("Currently Logged in: " + currentUser.getEmail());
        }
    }
    public void signInToGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                showToastMessage("Google Sign in Succeeded");
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                showToastMessage("Google Sign in Failed " + e);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.d(TAG, "signInWithCredential:success: currentUser: " + user.getEmail());
                            showToastMessage("Firebase Authentication Succeeded ");
                            launchMainActivity(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            showToastMessage("Firebase Authentication failed:" + task.getException());
                        }
                    }
                });
    }
    private void showToastMessage(String message) {
        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
    }
    private void launchMainActivity(FirebaseUser user) {
        if (user != null) {
            //LoginActivity.startActivity(this, user.getDisplayName());
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            intent.putExtra("user", user.getDisplayName());
            startActivity(intent);
        }
    }
}