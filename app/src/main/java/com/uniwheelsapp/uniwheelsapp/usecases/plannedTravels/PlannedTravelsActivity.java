package com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import com.uniwheelsapp.uniwheelsapp.databinding.ActivityMainBinding;
import com.uniwheelsapp.uniwheelsapp.databinding.ActivityPlannedTravelsBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
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

        person = (Person) getIntent().getExtras().get("person");
        viewModel = new ViewModelProvider(this).get(PlannedTravelsViewModel.class);
        viewModel.getTravelsByPerson(person);

        startFragment();
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