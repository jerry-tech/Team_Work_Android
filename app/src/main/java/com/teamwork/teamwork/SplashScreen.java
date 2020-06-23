package com.teamwork.teamwork;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        //calling the method that holds the delayed intent
        moveLogin();
    }
    public void moveLogin() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, Login.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            finish();
        },4000);
    }
}
