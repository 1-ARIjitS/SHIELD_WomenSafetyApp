package com.example.womensafety.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.womensafety.User.Detail_Forms;
import com.example.womensafety.R;
import com.google.android.material.navigation.NavigationView;

public class TravelLog extends AppCompatActivity {


    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    LinearLayout mTravelLogEvent;


    String vehicleNumber, travellingTo, travellingFrom, timeStarted, timeReached, address, date, estimatedTime;
    TextView tv_date, tv_travelFrom, tv_travelTo;
    Uri vehicleImageUri;



    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel_log);

        Intent intentGet = getIntent();
        Bundle bundle = intentGet.getExtras();


        tv_date = findViewById(R.id.tv_date);
        tv_travelFrom = findViewById(R.id.tv_travelFrom);
        tv_travelTo = findViewById(R.id.tv_travelTo);


        if(bundle != null){
            address = (String) bundle.get("address");
            timeReached = (String) bundle.get("timeStarted");
            timeStarted = (String) bundle.get("timeReached");
            date = (String) bundle.get("date");
            estimatedTime = (String) bundle.get("estimatedTime");
            vehicleNumber = (String) bundle.get("vehicleNumber");
            travellingFrom = (String) bundle.get("travellingFrom");
            travellingTo = (String) bundle.get("travellingTo");
            vehicleImageUri = (Uri) bundle.get("vehicleImageUri");
        }

        mTravelLogEvent = findViewById(R.id.travelLogEvent);
        mTravelLogEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(TravelLog.this, TravelLogContent.class);
                intent.putExtra("vehicleNumber", vehicleNumber+"");
                intent.putExtra("travellingFrom", travellingFrom+"");
                intent.putExtra("travellingTo", travellingTo+"");
                intent.putExtra("date", date+"");
                intent.putExtra("timeStarted", timeStarted+"");
                intent.putExtra("timeReached", timeReached+"");
                intent.putExtra("address", address+"");
                intent.putExtra("vehicleImageUri", vehicleImageUri);
                intent.putExtra("estimatedTime", estimatedTime+"");
                startActivity(intent);

            }
        });

        tv_date.setText(date);
        tv_travelTo.setText("to: "+travellingTo+"");
        tv_travelFrom.setText("from: "+travellingFrom+"");


        setUpToolbar();
        navigationView = findViewById(R.id.navigationMenu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.nav_travelLog:
                        break;

                    case R.id.nav_home:
                        startActivity(new Intent(TravelLog.this, AdminActivity.class));
                        break;

                    case R.id.travellingALone:
                        startActivity(new Intent(TravelLog.this, Detail_Forms.class));
                        break;

                    case R.id.nav_suspectRegistration:
                        startActivity(new Intent(TravelLog.this, SuspectRegistrationActivity.class));
                        break;
                    case R.id.nav_nextToKin:
                        startActivity(new Intent(TravelLog.this, NextToKinActivity.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }

/*    public void openLogDialog(){
        TravelLogDialog travelLogDialog = new TravelLogDialog();
        travelLogDialog.show(getSupportFragmentManager(), "Travel Log Dialog");
    }*/

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
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.textColor));
        actionBarDrawerToggle.syncState();
    }


}
