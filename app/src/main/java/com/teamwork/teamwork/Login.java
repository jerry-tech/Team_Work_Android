package com.teamwork.teamwork;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //used for showing time
        TextView timeLabel = findViewById(R.id.displayTime);
        Calendar calendar = Calendar.getInstance();
        timeLabel.setText(calendar.getTime().toString());
    }
}
