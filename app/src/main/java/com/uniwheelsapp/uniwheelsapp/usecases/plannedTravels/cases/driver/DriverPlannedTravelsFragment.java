package com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.cases.driver;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.uniwheelsapp.uniwheelsapp.R;

public class DriverPlannedTravelsFragment extends Fragment {

    private DriverPlannedTravelsViewModel viewModel;

    public static DriverPlannedTravelsFragment newInstance() {
        return new DriverPlannedTravelsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driver_planned_travels, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DriverPlannedTravelsViewModel.class);
        // TODO: Use the ViewModel
    }

}