package com.example.womensafety.SuperAdmin.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.womensafety.R;
import com.example.womensafety.SuperAdmin.Models.EmergencyContacts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManageEmergencyContactsActivity extends AppCompatActivity {

    EditText service,mob;
    Button add_contact;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_emergency_contacts);

        auth=FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Default_Emergency_Contacts");

        service=(EditText)findViewById(R.id.emergency_name);
        mob=(EditText)findViewById(R.id.emergency_mobile_number);
        add_contact=(Button)findViewById(R.id.emergency_add_button);

        add_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name,mobile;
                name=service.getText().toString();
                mobile=mob.getText().toString();

                EmergencyContacts contacts=new EmergencyContacts(name,mobile);

                if(name.isEmpty()&&mobile.isEmpty())
                {
                    Toast.makeText(ManageEmergencyContactsActivity.this, "INVALID,Blank Field", Toast.LENGTH_SHORT).show();
                }else if(name.isEmpty()){
                    Toast.makeText(ManageEmergencyContactsActivity.this, "INVALID,please enter the name of the emergency service", Toast.LENGTH_SHORT).show();
                }else if(name.isEmpty()){
                    Toast.makeText(ManageEmergencyContactsActivity.this, "INVALID,please enter an emergency contact", Toast.LENGTH_SHORT).show();
                }else{
                    reference.child(mobile).setValue(contacts);
                    Toast.makeText(getApplicationContext(), "Emergency contact successfully saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ManageEmergencyContactsActivity.this,SuperAdminHomepage.class));
                }

            }
        });
    }
}