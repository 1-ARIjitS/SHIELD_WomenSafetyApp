package com.example.womensafety.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.womensafety.R;
import com.example.womensafety.Models.kin_registered;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class NextToKinActivity extends AppCompatActivity {

    EditText kin_name;
    EditText kin_phone;
    Button kin_add;
    FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_to_kin);
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference("Next To kin");

        kin_name = findViewById(R.id.kin_name);
        kin_phone = findViewById(R.id.kin_mobile);
        kin_add = findViewById(R.id.kin_add_button);

        kin_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String k_name = kin_name.getText().toString();
                String k_phone = kin_phone.getText().toString();
                int c = 0;
                kin_registered kin = new kin_registered(k_name, k_phone);
                if (c >= 0 && c <= 4) {
                    if (k_name.isEmpty() && k_phone.isEmpty()) {
                        Toast.makeText(NextToKinActivity.this, "INVALID,Blank Field", Toast.LENGTH_SHORT).show();
                    } else if (k_name.isEmpty()) {
                        Toast.makeText(NextToKinActivity.this, "INVALID,please enter the name of next to kin", Toast.LENGTH_SHORT).show();
                    } else if (k_phone.isEmpty()) {
                        Toast.makeText(NextToKinActivity.this, "INVALID,please enter contact details of next to kin", Toast.LENGTH_SHORT).show();
                    } else {
                        reference.child(Objects.requireNonNull(auth.getCurrentUser()).getUid()).setValue(kin);
                        Toast.makeText(NextToKinActivity.this, "next to kin successfully added", Toast.LENGTH_SHORT).show();
                        c = c + 1;
                    }
                } else {
                    Toast.makeText(NextToKinActivity.this, "you have already added 5 next to kin contacts to your profile", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}