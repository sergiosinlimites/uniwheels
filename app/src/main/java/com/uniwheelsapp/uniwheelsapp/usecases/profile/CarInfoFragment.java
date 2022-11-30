package com.uniwheelsapp.uniwheelsapp.usecases.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.databinding.FragmentCarInfoBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Preferences;
import com.uniwheelsapp.uniwheelsapp.models.Vehiculo;
import com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.cases.driver.DriverPlannedTravelsViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarInfoFragment extends Fragment {

    private Person person;
    private Vehiculo vehiculo;
    private FragmentCarInfoBinding binding;
    private CarInfoViewModel viewModel;

    public CarInfoFragment() {

    }

    public static CarInfoFragment newInstance(String param1, String param2) {
        CarInfoFragment fragment = new CarInfoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewModel = new ViewModelProvider(this).get(CarInfoViewModel.class);
        binding = FragmentCarInfoBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        getUserInfo();
        setFields();
        return root;
    }

    private void getUserInfo() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Preferences.PREFERENCES, Context.MODE_PRIVATE);
        String personString = sharedPreferences.getString(Preferences.USER_INFO, "");
        person = gson.fromJson(personString, Person.class);
    }

    private void setFields(){
        vehiculo = person.getVehiculo();
        if(person.getHabilitado()){
            setViewFields();
        } else {
            setEditFields();
        }
    }

    private void setViewFields() {
        Log.d("HABILITADO", "MODO VISTA");
        binding.seeLayout.setVisibility(View.VISIBLE);
        binding.editLayout.setVisibility(View.GONE);
        if(vehiculo != null){
            binding.marcaView.setText(vehiculo.getMarca());
            binding.modeloView.setText(vehiculo.getModelo());
            if(vehiculo.getAnio() > 0){
                binding.anioView.setText(String.valueOf(vehiculo.getAnio()));
            }
            if(vehiculo.getCupos() > 0){
                binding.cuposView.setText(String.valueOf(vehiculo.getCupos()));
            }
        }
    }

    private void setEditFields() {
        Log.d("HABILITADO", "MODO EDICIÓN");
        binding.seeLayout.setVisibility(View.GONE);
        binding.editLayout.setVisibility(View.VISIBLE);
        if(vehiculo != null){
            binding.matricula.setText(vehiculo.getMatricula());
            binding.marca.setText(vehiculo.getMarca());
            binding.modelo.setText(vehiculo.getModelo());
            if(vehiculo.getAnio() > 0){
                binding.anio.setText(String.valueOf(vehiculo.getAnio()));
            }
            if(vehiculo.getCupos() > 0){
                binding.cupos.setText(String.valueOf(vehiculo.getCupos()));
            }
        }
        binding.actualizarVehiculo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("hola", "vehiculo");
                modificarInfo();
            }
        });
    }

    private void modificarInfo() {
        String matriculaString = binding.matricula.getText().toString();
        String marcaString = binding.marca.getText().toString();
        String modeloString = binding.modelo.getText().toString();
        String anioString = binding.anio.getText().toString();
        String cuposString = binding.cupos.getText().toString();
        Log.d("VEHICULO","ENTRA A MODIFICAR");
        if(
            matriculaString.isEmpty() ||
            marcaString.isEmpty() ||
            modeloString.isEmpty() ||
            anioString.isEmpty() ||
            cuposString.isEmpty()
        ) {
            Toast.makeText(getContext(), "No se han completado todos los campos necesarios", Toast.LENGTH_LONG).show();
            return;
        }
        vehiculo = new Vehiculo(matriculaString, "AUTOMOVIL", Integer.valueOf(cuposString), marcaString, modeloString, Integer.valueOf(anioString), false);

        viewModel.updateVehicle(person.getEmail(), vehiculo);
    }
}