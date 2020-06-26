package com.teamwork.teamwork;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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

        Button loginBtn = findViewById(R.id.btnLogin);
        loginBtn.setOnClickListener(event -> nextActivity());
    }
    public void nextActivity(){
        Intent intent = new Intent(this,UserOptions.class);
        startActivity(intent);

    }
}
