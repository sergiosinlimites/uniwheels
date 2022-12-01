package com.uniwheelsapp.uniwheelsapp.usecases.requests;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.adapters.AvailableTravelsAdapter;
import com.uniwheelsapp.uniwheelsapp.adapters.RequestedTravelsAdapter;
import com.uniwheelsapp.uniwheelsapp.databinding.ActivityRequestedTravelsBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Preferences;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;

import java.util.ArrayList;

public class RequestedTravelsActivity extends AppCompatActivity implements RequestedTravelsAdapter.RequestedTravelsClickListener {

    private ActivityRequestedTravelsBinding binding;

    private RequestTravelsViewModel viewModel;

    private Person person;

    private RequestedTravelsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_requested_travels);
        viewModel = new ViewModelProvider(this).get(RequestTravelsViewModel.class);
        View view = binding.getRoot();
        setContentView(view);
        getUserInfo();
        startRecyclerView();
        viewModel.getViajes().observe(this, new Observer<ArrayList<Viaje>>() {
            @Override
            public void onChanged(ArrayList<Viaje> viajes) {
                adapter.updateData(viajes);
            }
        });
    }

    private void startRecyclerView() {
        ArrayList<Viaje> viajesPlaneados = new ArrayList<>();
        binding.listaViajes.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new RequestedTravelsAdapter(viajesPlaneados, this);
        binding.listaViajes.setAdapter(adapter);
        viewModel.getTravelsFromDate();
    }

    private void getUserInfo() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Preferences.PREFERENCES, MODE_PRIVATE);
        String personString = sharedPreferences.getString(Preferences.USER_INFO, "");
        person = gson.fromJson(personString, Person.class);
        if(person == null || !person.getHabilitado()){
            Toast.makeText(this, "No estás habilitado, pronto uno de nuestros administradores verá tu solicitud", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onAcceptRequest(Viaje viaje) {
        android.app.AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage("¿Desea aceptar esta solicitud?")
                .setCancelable(false)
                .setPositiveButton("Sí, deseo aceptar la solicitud", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewModel.modificarSolicitud(viaje, "ACEPTADA");
                        // TODO toca notificar a los pasajeros que fue rechazado
                        adapter.removeRequest(viaje);
                    }
                })
                .setNegativeButton("No, regresar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }

    @Override
    public void onRejectRequest(Viaje viaje) {
        android.app.AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setMessage("¿Desea rechazar esta solicitud?")
                .setCancelable(false)
                .setPositiveButton("Sí, deseo rechazar la solicitud", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        viewModel.modificarSolicitud(viaje, "RECHAZADA");
                        // TODO toca notificar a los pasajeros que fue rechazado
                        adapter.removeRequest(viaje);
                    }
                })
                .setNegativeButton("No, regresar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }
}