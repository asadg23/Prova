package com.example.prova;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeScreen extends AppCompatActivity {

    TextView txtemail;
    Intent intent;
    String getemail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
    txtemail=findViewById(R.id.useremailshow);
intent=getIntent();
getemail=intent.getStringExtra("Email");
txtemail.setText("Welcome"+"    "+getemail.split("@")[0]);

    }
}
