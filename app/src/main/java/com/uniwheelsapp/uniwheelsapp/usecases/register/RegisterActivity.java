package com.uniwheelsapp.uniwheelsapp.usecases.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.RegisterInfo;
import com.uniwheelsapp.uniwheelsapp.databinding.ActivityRegisterBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Preferences;
import com.uniwheelsapp.uniwheelsapp.usecases.home.MainActivity;
import com.uniwheelsapp.uniwheelsapp.usecases.onboarding.EntranceActivity;
import com.uniwheelsapp.uniwheelsapp.usecases.onboarding.EntranceViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    private RegisterViewModel viewModel;

    private static final int COD_SEL_IMAGE = 300;

    private ProgressDialog progressDialog;

    private HashMap<String, Object> userObject;
    enum UserTypes {
        CONDUCTOR,
        PASAJERO
    }

    private Uri documentUrl;

    // Fields
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        userObject = new HashMap<>();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        progressDialog = new ProgressDialog(this);

        getUserInfo();

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class
        );

        viewModel.getAccountData().observe(this, new Observer<GoogleSignInAccount>() {
            @Override
            public void onChanged(GoogleSignInAccount googleSignInAccount) {
                if(googleSignInAccount != null){
                    email = googleSignInAccount.getEmail();
                    checkValidity(email);
                } else {
                    GoBack();
                }
            }
        });

        viewModel.getPerson().observe(this, new Observer<Person>() {
            @Override
            public void onChanged(Person person) {
                if(
                    person.getNombre().isEmpty() ||
                    person.getApellido().isEmpty() ||
                    person.getCedula() < 1 ||
                    person.getCelular() < 1
                ) {
                    setFields(person);
                    getImage(person);
                } else {
                    savePersonInfo(person);
                    MainActivity();
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

        binding.driverSectionLayout.setVisibility(View.INVISIBLE);

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
    }

    private void getUserInfo() {
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String personString = sharedPreferences.getString(Preferences.USER_INFO, "");
        Person person = gson.fromJson(personString, Person.class);
        if(person != null){
            if(person.getActivo()){
                MainActivity();
            }
        }
    }

    private void savePersonInfo(Person person) {
        Gson gson = new Gson();
        String personJSON = gson.toJson(person);
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Preferences.PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Preferences.USER_INFO, personJSON).commit();
    }

    /**
     * Actualiza la foto del documento
     */
    private void uploadPhoto(){
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, COD_SEL_IMAGE);
    }

    /**
     * Elimina la foto del documento
     */
    private void deletePhoto(){
        List<String> personDocuments = new ArrayList<>();
        personDocuments.add("");
        userObject.put(Person.IDPHOTOS_KEY, (List<String>) personDocuments);
        viewModel.updateUserDocument(email, userObject);
        deletePhotoFromView();
        Toast.makeText(this, "Foto del documento eliminada", Toast.LENGTH_SHORT).show();
    }

    /**
     * Elimina la foto de la vista
     */
    private void deletePhotoFromView(){
        binding.imageIdentificacion.setImageResource(android.R.drawable.ic_menu_upload);
    }

    /**
     * Revisa si el usuario ya se encuentra en la base de datos
     * @param email
     */
    private void checkValidity(String email){
        viewModel.searchInDB(email);
    }

    /**
     * Selecciona el tipo "Conductor"
     * @param view La vista
     */
    public void onSelectDriver(View view){
        binding.driverSectionLayout.setVisibility(View.VISIBLE);
        userObject.put(Person.USER_TYPE, UserTypes.CONDUCTOR.toString());
    }

    /**
     * Selecciona el tipo "Pasajero"
     * @param view La vista
     */
    public void onSelectPassenger(View view){
        binding.driverSectionLayout.setVisibility(View.INVISIBLE);
        userObject.put(Person.USER_TYPE, UserTypes.PASAJERO.toString());
        Log.d("SELECCION", userObject.get(Person.USER_TYPE).toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == COD_SEL_IMAGE){
                documentUrl = data.getData();
                subirFoto(documentUrl);
            }
        }
    }

    /**
     * Sube la foto a Firebase Storage
     * @param photoUrl La url del documento a partir de la data del intent.
     */
    private void subirFoto(Uri photoUrl){
        viewModel.updatePhoto(photoUrl);
    }

    /**
     * Pone todos los campos de la base de datos
     * @param person El documento de la base de datos del usuario
     */
    private void setFields(Person person){
        binding.signUpEmail.setText(person.getEmail() != null ? person.getEmail() : email);
        binding.signupNameInput.setText(person.getNombre());
        binding.signupLastNameInput.setText(person.getApellido());
        binding.signupAddressInput.setText(person.getDireccion());
        binding.signupCellphoneInput.setText(person.getCelular() > 1 ? String.valueOf(person.getCelular()) : "");
        binding.signupIdInput.setText(person.getCedula() > 1 ? String.valueOf(person.getCedula()) : "");
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
                    Picasso.with(RegisterActivity.this)
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
     * Acciones al pulsar continuar
     * @param view La vista
     */
    public void continueButton(View view){

        String nombre = binding.signupNameInput.getText().toString();
        String apellido = binding.signupLastNameInput.getText().toString();
        String direccion = binding.signupAddressInput.getText().toString();
        Number cellphone = binding.signupCellphoneInput.getText() != null ? Integer.parseInt(binding.signupCellphoneInput.getText().toString()) : null;
        Number identificacion = binding.signupIdInput.getText() != null ? Integer.parseInt(binding.signupIdInput.getText().toString()) : null;

        if(nombre.isEmpty() || apellido.isEmpty() || direccion.isEmpty() || cellphone.toString().isEmpty() || identificacion.toString().isEmpty()){
            Toast.makeText(this, "No se puede continuar, faltan campos por llenar", Toast.LENGTH_SHORT).show();
        } else {
            userObject.put(Person.NAME_KEY, nombre);
            userObject.put(Person.LASTNAME_KEY, apellido);
            userObject.put(Person.ADDRESS_KEY, direccion);
            userObject.put(Person.CELLPHONE_KEY, cellphone);
            userObject.put(Person.ID_KEY, identificacion);
            viewModel.updateUserDocument(email, userObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Continuando a la pantalla principal", Toast.LENGTH_SHORT).show();
                        MainActivity();
                    } else {
                        Log.w("ERROR", task.getException().toString());
                    }
                }
            });
        }
    }

    private void GoBack(){
        Intent intent = new Intent(this, EntranceActivity.class);
        startActivity(intent);
    }

    private void MainActivity(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}