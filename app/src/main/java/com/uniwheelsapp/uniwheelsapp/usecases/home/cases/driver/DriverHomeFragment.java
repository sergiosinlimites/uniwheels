package com.uniwheelsapp.uniwheelsapp.usecases.home.cases.driver;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.databinding.FragmentDriverHomeBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.PlannedTravelsActivity;
import com.uniwheelsapp.uniwheelsapp.usecases.profile.ProfileActivity;

import java.io.Serializable;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DriverHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DriverHomeFragment extends Fragment {

    private DriverHomeViewModel viewModel;
    private FragmentDriverHomeBinding binding;
    private Person person;

    public DriverHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DriverHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DriverHomeFragment newInstance(String param1, String param2) {
        DriverHomeFragment fragment = new DriverHomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(DriverHomeViewModel.class);
        binding = FragmentDriverHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        Bundle data = getArguments();
        if(data != null && data.getParcelable("person") != null){
            person = (Person) data.getParcelable("person");
            Log.d("entra ", "aqui");
            binding.welcomeText.setText("Bienvenido " + person.getNombre().toLowerCase(Locale.ROOT));
        };

        setOnClickListeners();
        return root;
    }

    private void setOnClickListeners(){
        binding.planTravelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), PlannedTravelsActivity.class);
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
}