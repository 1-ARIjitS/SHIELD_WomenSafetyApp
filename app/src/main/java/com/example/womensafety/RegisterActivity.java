package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hbb20.CountryCodePicker;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinner;

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

    public String mFull_name;
    public String mAge;
    public String mEmail_id;
    public String mMobile_number;
    public String mAddress;
    public String mPassword;
    public String mUVC;

    CountryCodePicker ccp;

    Button continue_button;

    Button already_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner=(Spinner)findViewById(R.id.gender_spinner);

        auth=FirebaseAuth.getInstance();

        database=FirebaseFirestore.getInstance();

        continue_button=(Button)findViewById(R.id.registration_verify_button);

        already_button=(Button)findViewById(R.id.registration_already_an_user);

        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.gender,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        full_name=(EditText)findViewById(R.id.registration_full_name);
        age=(EditText)findViewById(R.id.registration_age);
        mobile_number=(EditText)findViewById(R.id.registration_phone_number);
        address=(EditText)findViewById(R.id.registration_address);
        email_id=(EditText)findViewById(R.id.registration_email);
        confirm_unique=(EditText)findViewById(R.id.registration_confirm_uvc);
        unique=(EditText)findViewById(R.id.registration_uvc);
        confirm_password=(EditText)findViewById(R.id.registration_confirm_pass);
        password=(EditText)findViewById(R.id.registratin_pass);

        ccp.registerCarrierNumberEditText(mobile_number);

        mFull_name=full_name.getText().toString();
        mAge=age.getText().toString();
        mMobile_number=mobile_number.getText().toString();
        mEmail_id=email_id.getText().toString();
        mAddress=address.getText().toString();
        mPassword=password.getText().toString();
        mUVC=unique.getText().toString();

        User user=new User(mFull_name,mAge,mEmail_id,mMobile_number,mAddress,mPassword,mUVC);

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mMobile_number.replace(" ","").length()==0||mFull_name.length()==0||mEmail_id.length()==0||mAge.length()==0||mAddress.length()==0)
                {
                    Toast.makeText(getApplicationContext(),"INVALID,Blank Field",Toast.LENGTH_SHORT).show();
                }
                else if(mMobile_number.replace(" ","").length()<10)
                {
                    Toast.makeText(getApplicationContext(),"INVALID,phone number entered is too short",Toast.LENGTH_SHORT).show();
                }
                else if(mMobile_number.replace(" ","").length()>10)
                {
                    Toast.makeText(getApplicationContext(),"INVALID,phone number entered is too long",Toast.LENGTH_SHORT).show();
                }
                else if(mMobile_number.replace(" ","").length()>10)
                {
                    Toast.makeText(getApplicationContext(),"INVALID,phone number entered is too long",Toast.LENGTH_SHORT).show();
                }
                else if(mPassword!=confirm_password.getText().toString())
                {
                    Toast.makeText(getApplicationContext(),"INVALID,confirm password does not match password entered",Toast.LENGTH_SHORT).show();
                }
                else if(mUVC!=confirm_unique.getText().toString())
                {
                    Toast.makeText(getApplicationContext(),"INVALID,confirm unique verification code does not match unique verification code entered",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent;
                    intent=new Intent(RegisterActivity.this,LoginActivity.class);
                    intent.putExtra("phone_no.",ccp.getFullNumberWithPlus().replace(" ",""));
                    startActivity(intent);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




}