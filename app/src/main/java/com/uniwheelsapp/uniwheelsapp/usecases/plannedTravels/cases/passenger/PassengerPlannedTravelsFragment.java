package com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.cases.passenger;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.adapters.PlannedTravelsPassengerAdapter;
import com.uniwheelsapp.uniwheelsapp.databinding.FragmentPassengerPlannedTravelsBinding;
import com.uniwheelsapp.uniwheelsapp.models.PasajeroViaje;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;

import java.util.ArrayList;

public class PassengerPlannedTravelsFragment extends Fragment implements PlannedTravelsPassengerAdapter.PlannedTravelsClickListener {

    private PassengerPlannedTravelsViewModel viewModel;
    private FragmentPassengerPlannedTravelsBinding binding;

    private Person person;
    private PlannedTravelsPassengerAdapter adapter;

    public static PassengerPlannedTravelsFragment newInstance() {
        return new PassengerPlannedTravelsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PassengerPlannedTravelsViewModel.class);
        binding = FragmentPassengerPlannedTravelsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        person = (Person) getArguments().getParcelable("person");
        startRecyclerView();
        return inflater.inflate(R.layout.fragment_passenger_planned_travels, container, false);
    }

    private void startRecyclerView() {
        ArrayList<Viaje> viajesPlaneados = new ArrayList<Viaje>();
        binding.listaViajes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PlannedTravelsPassengerAdapter(viajesPlaneados, person, this);
        binding.listaViajes.setAdapter(adapter);
        viewModel.getTravelsByPassenger(adapter, person.getEmail());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PassengerPlannedTravelsViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onCancelTravel(Viaje viaje, Person personaViaje) {

    }
}