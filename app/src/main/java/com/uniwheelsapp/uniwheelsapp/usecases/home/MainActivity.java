package com.uniwheelsapp.uniwheelsapp.usecases.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.uniwheelsapp.uniwheelsapp.PlanTripsActivity;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.usecases.onboarding.EntranceActivity;

public class MainActivity extends AppCompatActivity {

    TextView name, email;
    Button logout;

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        System.out.println(token);
                        Toast.makeText(MainActivity.this, "Your device configuration token is" + token, Toast.LENGTH_SHORT).show();
                    }
                });

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
                startActivity(new Intent(getApplicationContext(), EntranceActivity.class));
            }
        });
    }
}