package com.uniwheelsapp.uniwheelsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;

import javax.annotation.Nullable;

public class EntranceActivity extends AppCompatActivity {

    Button registerButton;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);

        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundleAnalytics = new Bundle();
        bundleAnalytics.putString("message", "Integración con firebase");
        firebaseAnalytics.logEvent("InitScreen", bundleAnalytics);

        registerButton = findViewById(R.id.registerButton);
        gso = new GoogleSignInOptions.Builder()
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                SignIn();
            }
        });
    }

    public void onGoLogin(View view){
        throw new RuntimeException("Test Crash");
        // Intent i = new Intent(this, LoginActivity.class);
        // startActivity(i);
    }

    public void SignIn(){
        Intent intent = gsc.getSignInIntent();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                task.getResult(ApiException.class);
                System.out.println(task.getResult().getEmail());
                RegisterActivity();
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void RegisterActivity(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}