package com.example.womensafety;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;

import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.view.MenuItem;


import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;

public class Detail_Forms extends AppCompatActivity {

    Button Access_DisplayTime;
    private int CalenderHour, CalenderMinute;
    String format;
    Calendar calender;
    TimePickerDialog timePickerDialog;
    Button VehicleImageSelector;
    public static final int GET_FROM_GALLERY = 3;
    ImageView imageView;
    TextView timeTextView;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__forms);



        Access_DisplayTime = findViewById(R.id.timePicker);
        timeTextView = findViewById(R.id.timeTextView);

        Access_DisplayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calender = Calendar.getInstance();
                CalenderHour = calender.get(Calendar.HOUR_OF_DAY);
                CalenderMinute = calender.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(Detail_Forms.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay == 0) {
                            hourOfDay += 12;
                            format = "AM";
                        } else if (hourOfDay == 12) {
                            format = "PM";
                        } else if (hourOfDay > 12) {
                            hourOfDay -= 12;
                            format = "PM";
                        } else {
                            format = "AM";
                        }
                        timeTextView.setVisibility(View.VISIBLE);
                        timeTextView.setText("Estimated time: " + hourOfDay + ":" + minute + " " + format);
                    }
                }, CalenderHour, CalenderMinute, false );
                timePickerDialog.show();
            }
        });

        VehicleImageSelector = findViewById(R.id.vehicleImageSelector);
        VehicleImageSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI), GET_FROM_GALLERY);
            }

        });

        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_home:
                        startActivity(new Intent(Detail_Forms.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        break;
                        
                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(Detail_Forms.this, SuspectRegistrationActivity.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode == GET_FROM_GALLERY && resultCode == Activity.RESULT_OK){

            imageView = findViewById(R.id.vehicleImage);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(data.getData());
        }
    }



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.travelling_along_button, R.string.travelling_along_button);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        actionBarDrawerToggle.syncState();
    }

}
