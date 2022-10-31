package com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.cases.passenger;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uniwheelsapp.uniwheelsapp.R;

public class PassengerPlannedTravelsFragment extends Fragment {

    private PassengerPlannedTravelsViewModel viewModel;

    public static PassengerPlannedTravelsFragment newInstance() {
        return new PassengerPlannedTravelsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passenger_planned_travels, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(PassengerPlannedTravelsViewModel.class);
        // TODO: Use the ViewModel
    }

}