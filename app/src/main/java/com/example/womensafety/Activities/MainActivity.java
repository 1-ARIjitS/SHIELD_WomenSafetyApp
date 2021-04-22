package com.example.womensafety.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.womensafety.R;

public class MainActivity extends AppCompatActivity {

    public static final int SPLASH_SCREEN_TIMEOUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent loginIntent = new Intent(MainActivity.this, SelectUserActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, SPLASH_SCREEN_TIMEOUT);
    }
}