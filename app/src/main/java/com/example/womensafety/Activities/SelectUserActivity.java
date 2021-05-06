package com.example.womensafety.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.womensafety.R;

public class SelectUserActivity extends AppCompatActivity {
    Button user,admin,superadmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        superadmin=(Button)findViewById(R.id.superadmin);
        admin=(Button)findViewById(R.id.admin);
        user=(Button)findViewById(R.id.user);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AdminLoginActivity.class));
            }
        });
    }
}