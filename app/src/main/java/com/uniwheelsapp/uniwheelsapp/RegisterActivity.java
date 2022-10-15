package com.uniwheelsapp.uniwheelsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import javax.annotation.Nullable;

public class RegisterActivity extends AppCompatActivity {

    ImageView googleButton;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        googleButton = findViewById(R.id.googleButton);
        gso = new GoogleSignInOptions.Builder()
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this, gso);

        googleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                SignIn();
            }
        });
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
                MainActivity();
            } catch (ApiException e) {
                e.printStackTrace();
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void MainActivity(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}