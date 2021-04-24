package com.example.womensafety.SuperAdmin.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.womensafety.Activities.RegisterActivity;
import com.example.womensafety.Activities.SuspectListActivity;
import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.Admins;
import com.example.womensafety.User.Detail_Forms;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.Objects;

public class RegisterAdminActivity extends AppCompatActivity {

    MaterialSpinner spinner;
    EditText name,age,address,email,mobile,password;
    String m_gender;
    String m_name,m_age,m_address,m_email,m_mobile,m_password;
    Button create_new_admin_button;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);



        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("registered_admins");

        spinner = (MaterialSpinner) findViewById(R.id.create_admin_gender_spinner);
        spinner.setItems("Male", "Female");
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if(item=="Male")
                    m_gender="Male";
                else
                    m_gender="Female";

            }
        });

        create_new_admin_button=(Button)findViewById(R.id.create_admin_button);

        name=findViewById(R.id.create_admin_name);
        age=findViewById(R.id.create_admin_age);
        address=findViewById(R.id.create_admin_address);
        email=findViewById(R.id.create_admin_email);
        mobile=findViewById(R.id.create_admin_mob);
        password=findViewById(R.id.create_admin_password);

        create_new_admin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_name=name.getText().toString();
                m_address=address.getText().toString();
                m_age=age.getText().toString();
                m_email=email.getText().toString();
                m_mobile=mobile.getText().toString();
                m_password=password.getText().toString();

                final Admins admins=new Admins(m_name,m_age,m_address,m_email,m_mobile,m_gender,m_password);

                if (m_name.isEmpty() && m_age.isEmpty() && m_address.isEmpty() && m_email.isEmpty() && m_mobile.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "INVALID,Blank Fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (m_name.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an username", Toast.LENGTH_SHORT).show();
                    }
                    if (m_address.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an address", Toast.LENGTH_SHORT).show();
                    }
                    if (m_age.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an age", Toast.LENGTH_SHORT).show();
                    }
                    if (m_mobile.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter a mobile number", Toast.LENGTH_SHORT).show();
                    }
                    if (m_email.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter an email id", Toast.LENGTH_SHORT).show();
                    } else if (m_mobile.length() > 0 && m_mobile.length() < 10) {
                        Toast.makeText(getApplicationContext(), "INVALID,mobile number is too short", Toast.LENGTH_SHORT).show();
                    } else if (m_mobile.length() > 10) {
                        Toast.makeText(getApplicationContext(), "INVALID,mobile number is too long", Toast.LENGTH_SHORT).show();
                    } else if (Integer.parseInt(m_age) > 130 || Integer.parseInt(m_age) < 0) {
                        Toast.makeText(getApplicationContext(), "INVALID,please enter a valid age", Toast.LENGTH_SHORT).show();
                    } else {
                        auth.createUserWithEmailAndPassword(m_email, m_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    reference.child(m_mobile).setValue(admins);
                                    startActivity(new Intent(getApplicationContext(), ManageAdminActivity.class));
                                    Toast.makeText(getApplicationContext(), "A New Admin Account Is Successfully Created", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}