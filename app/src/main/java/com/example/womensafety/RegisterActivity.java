package com.example.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth auth;

    FirebaseFirestore database;

    EditText full_name;

    EditText age;

    EditText email_id;

    EditText mobile_number;

    EditText address;

    EditText unique;
    EditText confirm_unique;

    EditText password;
    EditText confirm_password;

   String mFull_name;
   String mAge;
   String mEmail_id;
   String mMobile_number;
   String mAddress;
   String mPassword;
   String mUVC;

    Button continue_button;

    Button already_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        auth=FirebaseAuth.getInstance();

        database=FirebaseFirestore.getInstance();

        full_name=findViewById(R.id.registration_full_name);

        email_id=findViewById(R.id.registration_email);

        age=findViewById(R.id.registration_age);

        mobile_number=findViewById(R.id.registration_phone_number);

        address=findViewById(R.id.registration_address);

        unique=findViewById(R.id.registration_uvc);

        confirm_unique=findViewById(R.id.registration_confirm_uvc);

        password=findViewById(R.id.registratin_pass);

        confirm_password=findViewById(R.id.registration_confirm_pass);

        already_button=findViewById(R.id.registration_already_an_user);

        continue_button=findViewById(R.id.registration_verify_button);

        already_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
            }
        });


        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFull_name=full_name.getText().toString();
                mUVC=unique.getText().toString();
                mPassword=password.getText().toString();
                mAddress=address.getText().toString();
                mMobile_number=mobile_number.getText().toString();
                mEmail_id=email_id.getText().toString();
                mAge=age.getText().toString();

                final User user=new User();

                user.setAddress(mAddress);
                user.setAge(mAge);
                user.setFull_name(mFull_name);
                user.setPassword(mPassword);
                user.setUVC(mUVC);
                user.setMobile_number(mMobile_number);
                user.setEmail_id(mEmail_id);

                if(mMobile_number.isEmpty()||mUVC.isEmpty()||mPassword.isEmpty()||mEmail_id.isEmpty()||mFull_name.isEmpty()||mAddress.isEmpty()||mAge.isEmpty())
                {
                    Toast.makeText(RegisterActivity.this,"INVALID,Blank Field",Toast.LENGTH_SHORT).show();
                }
                else if(mMobile_number.length()<10)
                {
                    Toast.makeText(RegisterActivity.this,"INVALID,mobile number entered is too short",Toast.LENGTH_SHORT).show();
                }
                else if(mMobile_number.length()>10)
                {
                    Toast.makeText(RegisterActivity.this,"INVALID,mobile number entered is too long",Toast.LENGTH_SHORT).show();
                }
                else if(mPassword.length()!=confirm_password.getText().toString().length())
                {
                    Toast.makeText(RegisterActivity.this,"INVALID,password does not match confirm password",Toast.LENGTH_SHORT).show();
                }
                else if(mUVC.length()!=confirm_unique.getText().toString().length())
                {
                    Toast.makeText(RegisterActivity.this,"INVALID,unique verification password does not match confirm unique verification password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    auth.createUserWithEmailAndPassword(mEmail_id,mPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                database.collection("User").document().set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                                            }
                                        });
                                Toast.makeText(RegisterActivity.this,"Account successfully created",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });



    }
}
