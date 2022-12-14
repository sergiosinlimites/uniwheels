package com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.cases.driver;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.adapters.PlannedTravelsAdapter;
import com.uniwheelsapp.uniwheelsapp.databinding.FragmentDriverHomeBinding;
import com.uniwheelsapp.uniwheelsapp.databinding.FragmentDriverPlannedTravelsBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;
import com.uniwheelsapp.uniwheelsapp.usecases.home.cases.driver.DriverHomeViewModel;
import com.uniwheelsapp.uniwheelsapp.usecases.maps.MapsActivity;
import com.uniwheelsapp.uniwheelsapp.usecases.newTravel.NewTravelActivity;
import com.uniwheelsapp.uniwheelsapp.usecases.newTravel.NewTravelViewModel;

import java.util.ArrayList;

public class DriverPlannedTravelsFragment extends Fragment implements PlannedTravelsAdapter.PlannedTravelsClickListener {

    private DriverPlannedTravelsViewModel viewModel;
    private FragmentDriverPlannedTravelsBinding binding;
    private Person person;
    private PlannedTravelsAdapter adapter;

    public static DriverPlannedTravelsFragment newInstance() {
        return new DriverPlannedTravelsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(DriverPlannedTravelsViewModel.class);
        binding = FragmentDriverPlannedTravelsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        binding.newTravelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewTravel();
            }
        });
        person = (Person) getArguments().getParcelable("person");
        startRecyclerView();

        return root;
    }

    private void startRecyclerView() {
        ArrayList<Viaje> viajesPlaneados = new ArrayList<>();
        binding.listaViajes.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PlannedTravelsAdapter(viajesPlaneados, this);
        binding.listaViajes.setAdapter(adapter);
        viewModel.getTravelsByDriver(adapter, person.getEmail());
    }


    @Override
    public void onCancelTravel(Viaje viaje) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(getContext());
        alerta.setMessage("??Desea cancelar este viaje?")
                .setCancelable(false)
                .setPositiveButton("S??, deseo cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("ID",viaje.getDocumentId());
                        viewModel.cancelTravel(viaje.getDocumentId());
                        // TODO toca notificar a los pasajeros que fue cancelado
                        adapter.cancelTravel(viaje);
                        Toast.makeText(getContext(), "Viaje cancelado exitosamente", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No, regresar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        alerta.show();
    }

    public void createNewTravel(){
        Intent intent = new Intent(getActivity().getApplicationContext(), NewTravelActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSeeDetailsFromTravel(Viaje viaje) {

    }
}