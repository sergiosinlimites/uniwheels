package com.uniwheelsapp.uniwheelsapp.usecases.profile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.databinding.ActivityProfileBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Preferences;
import com.uniwheelsapp.uniwheelsapp.usecases.onboarding.EntranceActivity;
import com.uniwheelsapp.uniwheelsapp.usecases.register.RegisterActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    private static final int COD_SEL_IMAGE = 300;

    private ActivityProfileBinding binding;
    private ProfileViewModel viewModel;
    private Person person;
    private Gson gson;
    private SharedPreferences sharedPreferences;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(this);

        gson = new Gson();

        sharedPreferences = getApplicationContext().getSharedPreferences(Preferences.PREFERENCES, MODE_PRIVATE);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        viewModel.getPersonMutableLiveData().observe(this, new Observer<Person>() {
            @Override
            public void onChanged(Person newPerson) {
                person = newPerson;
                setUserInfo(person);
                if(person != null && !person.getActivo()){
                    goToEntrance();
                }
            }
        });

        viewModel.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    progressDialog.setMessage("Cargando");
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            }
        });

        binding.buttonUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPhoto();
            }
        });

        binding.buttonDeleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletePhoto();
            }
        });

        getUserInfo();
    }

    private void setUserInfo(Person person) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String personJSON = gson.toJson(person);
        editor.putString(Preferences.USER_INFO, personJSON);
        editor.commit();
    }

    /**
     * Actualiza la foto del documento
     */
    private void uploadPhoto(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }

    private void setFields() {
        if(person != null){
            binding.nombre.setText(person.getNombre());
            binding.apellido.setText(person.getApellido());
            binding.id.setText(String.valueOf(person.getCedula()));
            binding.correo.setText(person.getEmail());
            binding.celular.setText(String.valueOf(person.getCelular()));
            binding.direccion.setText(person.getDireccion());
            binding.ModificarPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            binding.EliminarCuenta.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alerta = new AlertDialog.Builder(ProfileActivity.this);
                    alerta.setMessage("¿Desea eliminar su cuenta?")
                            .setCancelable(false)
                            .setPositiveButton("Sí, deseo eliminarla", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    viewModel.setInactive(person);
                                    Toast.makeText(getApplicationContext(), "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No, regresar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    return;
                                }
                            });
                    alerta.show();
                }
            });
            if(person.getTipo().equals("CONDUCTOR")){
                FragmentManager manager = getSupportFragmentManager();
                CarInfoFragment carInfoFragment = new CarInfoFragment();
                Log.d("FRAGMENT", "LO HACE");
                manager.beginTransaction().replace(binding.replaceableLayout.getId(), carInfoFragment).commit();
            }
        }
    }

    private void getUserInfo() {
        String personString = sharedPreferences.getString(Preferences.USER_INFO, "");
        Log.d("PERSONA", personString);
        person = gson.fromJson(personString, Person.class);
        Log.d("ACTIVO", String.valueOf(person.getActivo()));
        if(person != null && !person.getActivo()){
            goToEntrance();
        }
        viewModel.listenForChanges(person.getEmail());
        setFields();
        getImage(person);
    }

    /**
     * Muestra la imagen del documento de identidad
     */
    private void getImage(Person person){
        List<String> photos = person.getFotosCedula();
        if(photos.size() > 0){
            String documentPhoto = photos.get(0);
            if(documentPhoto != null){
                try {
                    Picasso.with(ProfileActivity.this)
                        .load(documentPhoto)
                        .resize(150, 150)
                        .into(binding.imageIdentificacion);
                } catch (Exception e) {
                    Log.w("ERROR", e);
                }
            }
        }
    }

    /**
     * Elimina la foto del documento
     */
    private void deletePhoto(){
        List<String> personDocuments = new ArrayList<>();
        personDocuments.add("");
        person.setFoto("");
        Map<String, Object> userObject = new HashMap<>();
        userObject.put("foto", person.getFoto());
        viewModel.updateUserDocument(person.getEmail(), userObject);
        deletePhotoFromView();
        Toast.makeText(this, "Foto del documento eliminada", Toast.LENGTH_SHORT).show();
    }

    private void goToEntrance() {
        Intent intent = new Intent(this, EntranceActivity.class);
        finish();
        startActivity(intent);
    }

    /**
     * Elimina la foto de la vista
     */
    private void deletePhotoFromView(){
        binding.imageIdentificacion.setImageResource(android.R.drawable.ic_menu_upload);
    }
}
