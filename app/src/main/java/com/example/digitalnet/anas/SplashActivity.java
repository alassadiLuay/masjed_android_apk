package com.example.digitalnet.anas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences;
import types.Listener;


public class SplashActivity extends Activity {

    private final int splash_display_length = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String MY_PREFS_NAME = "log_in";
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                String user_name = prefs.getString("userName", null);
                String password = prefs.getString("password", null);
                String listener_id = prefs.getString("listener_id", null);

                if (user_name != null && password != null && listener_id != null) {
                    System.out.println("louai the user is logging in user name " + user_name + " password " + password + " listener id " + listener_id );
                    Listener listener = new Listener(prefs.getString("userName",null),prefs.getString("password", null), prefs.getString("listener_id", null));
                    Intent mainIntent = new Intent(SplashActivity.this , SemesterActivity.class);
                    startActivity(mainIntent);
                    finish();

                }
                else{
                    Intent loginIntent = new Intent(SplashActivity.this,LoginActivity.class);
                    SplashActivity.this.startActivity(loginIntent);
                    SplashActivity.this.finish();
                }


            }
        },splash_display_length);
    }
}
