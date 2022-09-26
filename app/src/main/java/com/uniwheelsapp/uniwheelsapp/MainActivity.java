package com.uniwheelsapp.uniwheelsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onGoNow(View view){
        Intent i = new Intent(this, AvailableTravelsActivity.class);
        startActivity(i);
    }

    public void onPlanRide(View view){
        Intent i = new Intent(this, AvailableTravelsActivity.class);
        startActivity(i);
    }
}