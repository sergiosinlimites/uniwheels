package com.uniwheelsapp.uniwheelsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    TextView name, email;
    Button logout;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        System.out.println("HACE ESTOOOOO");
        if(account != null){
            String name = account.getDisplayName();
            String mail = account.getEmail();
            System.out.println("Nombre " + name);
            System.out.println("Email " + mail);
        }

        logout = findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });
    }

    public void onGoNow(View view){
        Intent i = new Intent(this, PlanTripsActivity.class);
        startActivity(i);
    }

    public void onPlanRide(View view){
        try {
            System.out.println("entra");
            Intent i = new Intent(this, PlanTripsActivity.class);
            startActivity(i);
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private void SignOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(getApplicationContext(),EntranceActivity.class));
            }
        });
    }
}