package com.uniwheelsapp.uniwheelsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;

public class EntranceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundleAnalytics = new Bundle();
        bundleAnalytics.putString("message", "Integración con firebase");
        firebaseAnalytics.logEvent("InitScreen", bundleAnalytics);
    }

    public void onGoLogin(View view){
        throw new RuntimeException("Test Crash");
        // Intent i = new Intent(this, LoginActivity.class);
        // startActivity(i);
    }

    public void onGoRegister(View view){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}