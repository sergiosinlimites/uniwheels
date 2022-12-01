package com.uniwheelsapp.uniwheelsapp.usecases.home.cases.passenger;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.uniwheelsapp.uniwheelsapp.PlanTripsActivity;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.databinding.FragmentDriverHomeBinding;
import com.uniwheelsapp.uniwheelsapp.databinding.FragmentPassengerHomeBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.usecases.onboarding.EntranceActivity;
import com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.PlannedTravelsActivity;
import com.uniwheelsapp.uniwheelsapp.usecases.profile.ProfileActivity;
import com.uniwheelsapp.uniwheelsapp.usecases.searchTravel.SearchTravelActivity;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PassengerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PassengerHomeFragment extends Fragment {
    private View root;
    private FragmentPassengerHomeBinding binding;
    private PassengerHomeViewModel viewModel;
    private Button logout;
    private Person person;


    public PassengerHomeFragment() {
        // Required empty public constructor
    }

    public static PassengerHomeFragment newInstance(String param1, String param2) {
        PassengerHomeFragment fragment = new PassengerHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PassengerHomeViewModel.class);
        binding = FragmentPassengerHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle data = getArguments();
        if(data != null && data.getParcelable("person") != null){
            person = (Person) data.getParcelable("person");
        };

        setOnClickListeners();
        return root;
    }

    private void setOnClickListeners() {
        binding.planTravelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), SearchTravelActivity.class);
                startActivity(intent);
            }
        });
        binding.reviewRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        binding.profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                Intent intent = new Intent(getActivity().getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onGoNow(View view){
        Intent i = new Intent(getContext(), PlanTripsActivity.class);
        startActivity(i);
    }

    public void onPlanRide(View view){
        try {
            System.out.println("entra");
            Intent i = new Intent(getContext(), PlanTripsActivity.class);
            startActivity(i);
        } catch (Exception exception) {
            System.out.println(exception);
        }
    }

    private void SignOut(){
//        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                finish();
//                startActivity(new Intent(getApplicationContext(), EntranceActivity.class));
//            }
//        });
    }
}