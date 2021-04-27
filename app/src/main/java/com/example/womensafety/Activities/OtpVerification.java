package com.example.womensafety.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.womensafety.Models.users;
import com.example.womensafety.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OtpVerification extends AppCompatActivity {
    String phoneNumber;
    Button btnSignIn;
    Button editMobile;
    private static final String TAG = "OTPVerification";
    EditText etOTP;
    String mEmail_id;
    String mPassword;
    String otp;
    String mFull_name;
    String mAddress;
    String mUVC;
    String mAge;
    String country_code;
    String country;
    String state;
    String city;
    users users;

    FirebaseAuth auth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verification);
        findViews();

        auth = FirebaseAuth.getInstance();

        rootNode = FirebaseDatabase.getInstance();

        reference = rootNode.getReference("registered_users");
        StartFirebaseLogin();
        Intent intent = getIntent();
        phoneNumber = intent.getStringExtra("mobile");
        mEmail_id = intent.getStringExtra("mEmail_id");
        mPassword = intent.getStringExtra("mPassword");
        mFull_name = intent.getStringExtra("mFull_name");
        mAge = intent.getStringExtra("mAge");
        mAddress = intent.getStringExtra("mAddress");
        mUVC = intent.getStringExtra("mPassword");
        country_code = intent.getStringExtra("countryCode");
        country = intent.getStringExtra("country");
        state = intent.getStringExtra("state");
        city = intent.getStringExtra("city");


        users = new users(mFull_name, mAge, mEmail_id, phoneNumber, mAddress, mPassword, mUVC,country,state,city);

        //users = new users(mFull_name, mAge, mEmail_id, phoneNumber, mAddress, mPassword, mUVC);
        //phoneNumber="+919336079804";
        phoneNumber="+91"+phoneNumber;
        Log.d(TAG, phoneNumber);
        Toast.makeText(OtpVerification.this,"Please wait for 15 seconds for OTP.",Toast.LENGTH_SHORT).show();

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,                     // Phone number to verify
                60,                           // Timeout duration
                TimeUnit.SECONDS,                // Unit of timeout
                OtpVerification.this,        // Activity (for callback binding)
                mCallback);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp=etOTP.getText().toString();
                if(otp.isEmpty()){
                    Toast.makeText(OtpVerification.this,"OPT can not be empty!",Toast.LENGTH_SHORT).show();
                }else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);

                    SignInWithPhone(credential);
                    //linkAccount();
                }


            }
        });
        editMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(OtpVerification.this, RegisterActivity.class));
            }
        });

    }

    private void SignInWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(OtpVerification.this,"Verification Successfully!",Toast.LENGTH_SHORT).show();
                            auth.createUserWithEmailAndPassword(mEmail_id, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        reference.child(auth.getCurrentUser().getUid()).setValue(users);
                                        startActivity(new Intent(OtpVerification.this, AdminActivity.class));
                                    } else {
                                        Toast.makeText(OtpVerification.this, Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            //startActivity(new Intent(OtpVerification.this,LoginActivity.class));
                            finish();
                        } else {

                            // Show a message and update the UI
                            Toast.makeText(OtpVerification.this,"Incorrect OTP! Try again.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void findViews() {
        btnSignIn=findViewById(R.id.verify_button);

        etOTP=findViewById(R.id.etOtp);
        editMobile=findViewById(R.id.edit_mobile);
    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //startActivity(new Intent(OtpVerification.this, AdminActivity.class));
                SignInWithPhone(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(OtpVerification.this,"Invalid Entries entered",Toast.LENGTH_SHORT).show();

                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(OtpVerification.this,"The SMS quota for the project has been exceeded",Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(OtpVerification.this,"Some error occurred, Please try after a while",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(OtpVerification.this,"Code sent",Toast.LENGTH_SHORT).show();
            }
        };
    }
}