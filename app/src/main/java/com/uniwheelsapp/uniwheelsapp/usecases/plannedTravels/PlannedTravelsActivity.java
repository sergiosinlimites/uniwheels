package com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.uniwheelsapp.uniwheelsapp.databinding.ActivityMainBinding;
import com.uniwheelsapp.uniwheelsapp.databinding.ActivityPlannedTravelsBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Preferences;
import com.uniwheelsapp.uniwheelsapp.usecases.home.cases.admin.AdminHomeFragment;
import com.uniwheelsapp.uniwheelsapp.usecases.home.cases.driver.DriverHomeFragment;
import com.uniwheelsapp.uniwheelsapp.usecases.home.cases.passenger.PassengerHomeFragment;
import com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.cases.driver.DriverPlannedTravelsFragment;

public class PlannedTravelsActivity extends AppCompatActivity {

    private ActivityPlannedTravelsBinding binding;
    private PlannedTravelsViewModel viewModel;
    private Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlannedTravelsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getUserInfo();

        viewModel = new ViewModelProvider(this).get(PlannedTravelsViewModel.class);
        viewModel.getTravelsByPerson(person);

    }

    private void getUserInfo() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Preferences.PREFERENCES, MODE_PRIVATE);
        String personString = sharedPreferences.getString(Preferences.USER_INFO, "");
        Log.d("persona", personString);
        person = gson.fromJson(personString, Person.class);
        if(person != null){
            Log.d(person.getApellido(), person.getTipo());
            startFragment();
        }
    }

    private void startFragment(){
        FragmentManager manager = getSupportFragmentManager();
        if (person.getTipo().toString().equals("CONDUCTOR")){
            Bundle bundle = new Bundle();
            bundle.putParcelable("person", (Parcelable) person);
            DriverPlannedTravelsFragment driverHomeFragment = new DriverPlannedTravelsFragment();
            driverHomeFragment.setArguments(bundle);
            manager.beginTransaction().replace(binding.repleaceableLayout.getId(), driverHomeFragment).commit();
        } else if (person.getTipo().toString().equals("PASAJERO")){
//            PassengerHomeFragment passengerHomeFragment = new PassengerHomeFragment();
//            manager.beginTransaction().replace(binding.repleaceableLayout.getId(), passengerHomeFragment).commit();
        } else if (person.getTipo().toString().equals("ADMIN")){
//            AdminHomeFragment adminHomeFragment = new AdminHomeFragment();
//            manager.beginTransaction().replace(binding.repleaceableLayout.getId(), adminHomeFragment).commit();
        }
    }
}