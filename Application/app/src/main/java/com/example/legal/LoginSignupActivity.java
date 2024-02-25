package com.example.legal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class LoginSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        getSupportActionBar().hide();
    }
}