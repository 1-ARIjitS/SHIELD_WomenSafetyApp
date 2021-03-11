package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class SuspectRegistrationActivity extends AppCompatActivity {

    EditText suspect_name,suspect_description,suspect_mobile,suspect_identity;
    Button suspect_register;
    FirebaseAuth auth;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suspect_registration);

        auth=FirebaseAuth.getInstance();

        rootNode=FirebaseDatabase.getInstance();
        reference=rootNode.getReference("suspects_registered");

        suspect_name=findViewById(R.id.suspect_name);
        suspect_description=findViewById(R.id.suspect_description);
        suspect_mobile=findViewById(R.id.suspect_phone_number);
        suspect_identity=findViewById(R.id.suspect_specific_identity);
        suspect_register=findViewById(R.id.button);

        suspect_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String s_name=suspect_name.getText().toString();
                String s_description=suspect_description.getText().toString();
                String s_identity=suspect_identity.getText().toString();
                String s_mobile_num=suspect_mobile.getText().toString();

                suspect_registered sus=new suspect_registered(s_name,s_description,s_identity,s_mobile_num);
                reference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).setValue(sus);
                Toast.makeText(getApplicationContext(),"suspect registration successful",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SuspectRegistrationActivity.this,AdminActivity.class));
            }
        });

    }
}
