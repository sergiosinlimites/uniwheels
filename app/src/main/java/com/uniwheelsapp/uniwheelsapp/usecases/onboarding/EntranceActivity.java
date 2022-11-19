package com.uniwheelsapp.uniwheelsapp.usecases.onboarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Preferences;
import com.uniwheelsapp.uniwheelsapp.usecases.home.MainActivity;
import com.uniwheelsapp.uniwheelsapp.usecases.register.RegisterActivity;

import javax.annotation.Nullable;

public class EntranceActivity extends AppCompatActivity {

    private EntranceViewModel viewModel;

    Button registerButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrance);
        viewModel = new ViewModelProvider(this).get(EntranceViewModel.class);

        getUserInfo();

        viewModel.getAccountData().observe(this, new Observer<GoogleSignInAccount>() {
            @Override
            public void onChanged(GoogleSignInAccount googleSignInAccount) {
                if(googleSignInAccount != null){
                    Log.d("ENTRADA", "NO ES NULOO " + googleSignInAccount.getEmail());
                } else {
                    Log.d("ENTRADA", "ES NULO");
                }
            }
        });

        viewModel.getLoggedStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    RegisterActivity();
                }
            }
        });

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
                        Toast.makeText(EntranceActivity.this, "Your device configuration token is" + token, Toast.LENGTH_SHORT).show();
                    }
                });


//        FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics.getInstance(this);
//        Bundle bundleAnalytics = new Bundle();
//        bundleAnalytics.putString("message", "Integración con firebase");
//        firebaseAnalytics.logEvent("InitScreen", bundleAnalytics);

        registerButton = findViewById(com.uniwheelsapp.uniwheelsapp.R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                SignIn();
            }
        });
    }

    private void getUserInfo() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String personString = sharedPreferences.getString(Preferences.USER_INFO, "");
        Person person = gson.fromJson(personString, Person.class);
        if(person != null){
            if(person.getActivo()){
                goToMainActivity();
            }
        }
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
        startActivity(intent);
    }

    public void SignIn(){
        Log.d("ABRE LOGIN", "LOGIN ");
        Intent intent = viewModel.entrance();
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        Log.d("LOGIN", "HACE ESTOOOOO " + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100){
            viewModel.updateAccount(data);
        }
    }

    public void RegisterActivity(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}