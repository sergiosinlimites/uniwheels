package com.uniwheelsapp.uniwheelsapp.usecases.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.uniwheelsapp.uniwheelsapp.PlanTripsActivity;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.databinding.ActivityMainBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.usecases.home.cases.admin.AdminHomeFragment;
import com.uniwheelsapp.uniwheelsapp.usecases.home.cases.driver.DriverHomeFragment;
import com.uniwheelsapp.uniwheelsapp.usecases.home.cases.passenger.PassengerHomeFragment;
import com.uniwheelsapp.uniwheelsapp.usecases.onboarding.EntranceActivity;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity {

    private String email;

    private MainViewModel viewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        viewModel.getAccount().observe(this, new Observer<GoogleSignInAccount>() {
            @Override
            public void onChanged(GoogleSignInAccount googleSignInAccount) {
                if(googleSignInAccount != null){
                    email = googleSignInAccount.getEmail();
                    checkValidity(email);
                } else {
                    GoBack();
                }
            }
        });

        viewModel.getPersonMutableLiveData().observe(this, new Observer<Person>() {
            @Override
            public void onChanged(Person person) {
                FragmentManager manager = getSupportFragmentManager();
                Log.d("TIPO DE USUARIO", person.getTipo().toString());
                if (person.getTipo().toString().equals("CONDUCTOR")){
                    Log.d("CONDUCTOR", "ENTRA");
                    Bundle bundle = new Bundle();
                    bundle.putString("prueba", "funciona");
                    bundle.putParcelable("person", (Parcelable) person);
                    DriverHomeFragment driverHomeFragment = new DriverHomeFragment();
                    driverHomeFragment.setArguments(bundle);
                    manager.beginTransaction().replace(binding.replaceableLayout.getId(), driverHomeFragment).commit();
                } else if (person.getTipo().toString().equals("PASAJERO")){
                    PassengerHomeFragment passengerHomeFragment = new PassengerHomeFragment();
                    manager.beginTransaction().replace(binding.replaceableLayout.getId(), passengerHomeFragment).commit();
                } else if (person.getTipo().toString().equals("ADMIN")){
                    AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
                    manager.beginTransaction().replace(binding.replaceableLayout.getId(), adminHomeFragment).commit();
                }
            }
        });
    }

    private void checkValidity(String email){
        viewModel.searchInDB(email);
    }

    private void GoBack(){
        Intent intent = new Intent(this, EntranceActivity.class);
        startActivity(intent);
    }

}