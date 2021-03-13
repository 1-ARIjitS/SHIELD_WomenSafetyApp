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
import com.example.womensafety.Models.users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;

    FirebaseDatabase rootNode;
    DatabaseReference reference;

    EditText full_name;

    EditText age;

    EditText email_id;

    EditText mobile_number;

    EditText address;

    EditText unique;
    EditText confirm_unique;

    EditText password;
    EditText confirm_password;

    Button continue_button;

    Button already_button;

    CountryCodePicker ccp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth=FirebaseAuth.getInstance();

        rootNode=FirebaseDatabase.getInstance();

        reference=rootNode.getReference("registered_users");

        full_name=findViewById(R.id.registration_full_name);

        email_id=findViewById(R.id.registration_email);

        age=findViewById(R.id.registration_age);

        mobile_number=findViewById(R.id.registration_phone_number);

        address=findViewById(R.id.registration_address);

        unique=findViewById(R.id.registration_uvc);
        confirm_unique=findViewById(R.id.registration_confirm_uvc);

        password=findViewById(R.id.registration_pass);
        confirm_password=findViewById(R.id.registration_confirm_pass);

        already_button=findViewById(R.id.registration_already_an_user);

        continue_button=findViewById(R.id.registration_verify_button);

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mFull_name=full_name.getText().toString();
                String mAge=age.getText().toString();
                String mEmail_id=email_id.getText().toString();
                String mMobile_number=mobile_number.getText().toString();
                String mAddress=address.getText().toString();
                String mPassword=password.getText().toString();
                String mUVC=unique.getText().toString();

                final users users=new users(mFull_name,mAge,mEmail_id,mMobile_number,mAddress,mPassword,mUVC);

                if (mFull_name.isEmpty() && mAge.isEmpty() && mEmail_id.isEmpty() && mMobile_number.isEmpty() && mAddress.isEmpty() && mPassword.isEmpty() && mUVC.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "INVALID,Blank Field", Toast.LENGTH_SHORT).show();
                } else if (mMobile_number.length() < 10) {
                    Toast.makeText(RegisterActivity.this, "INVALID,mobile number entered is too short", Toast.LENGTH_SHORT).show();
                } else if (mMobile_number.length() > 10) {
                    Toast.makeText(RegisterActivity.this, "INVALID,mobile number entered is too long", Toast.LENGTH_SHORT).show();
                } else if (mPassword.length() != confirm_password.getText().toString().length()) {
                    Toast.makeText(RegisterActivity.this, "INVALID,password does not match confirm password", Toast.LENGTH_SHORT).show();
                } else if (mUVC.length() != confirm_unique.getText().toString().length()) {
                    Toast.makeText(RegisterActivity.this, "INVALID,unique verification password does not match confirm unique verification password", Toast.LENGTH_SHORT).show();
                } else {
                    auth.createUserWithEmailAndPassword(mEmail_id, mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                   reference.child(auth.getCurrentUser().getUid()).setValue(users);
                                   startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        already_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });
    }
}
