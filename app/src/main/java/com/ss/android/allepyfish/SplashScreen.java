package com.ss.android.allepyfish;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ss.android.allepyfish.activities.LoginActivity;
import com.ss.android.allepyfish.activities.ManagerLandingScreen;
import com.ss.android.allepyfish.activities.UploadNewFish;

public class SplashScreen extends AppCompatActivity {

    int SPLASH_TIME_OUT = 3000;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
// Normal Splash Screen no screen record added
//                Intent i = new Intent(SplashScreen.this, UploadNewFish.class);
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
//                Intent i = new Intent(SplashScreen.this, UploadNewFish.class);
//                Intent i = new Intent(SplashScreen.this, ManagerLandingScreen.class);
                startActivity(i);

                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
