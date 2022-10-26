package com.uniwheelsapp.uniwheelsapp.usecases.register;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.squareup.picasso.Picasso;
import com.uniwheelsapp.uniwheelsapp.R;
import com.uniwheelsapp.uniwheelsapp.RegisterInfo;
import com.uniwheelsapp.uniwheelsapp.databinding.ActivityRegisterBinding;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.usecases.home.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    // Firebase Storage
    StorageReference storageReference;
    String storagePath = "documents/*";

    private static final int COD_SEL_IMAGE = 300;
    private Uri documentUrl;
    String photo = "photo";

    ProgressDialog progressDialog;

    // Firebase Auth
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    FirebaseAuth mAuth;
    String userId;

    // Firebase FireStore
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference userDocRef = null;

    // Fields
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.uniwheelsapp.uniwheelsapp.R.layout.activity_register);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        mAuth = FirebaseAuth.getInstance();

        if(account != null){
            userId = account.getId();
            email = account.getEmail();
            System.out.println("El email es "+ email.toString());
            userDocRef = db.collection("users").document(email);
            checkValidity(email);
        }

        binding.driverSectionLayout.setVisibility(View.INVISIBLE);

        progressDialog = new ProgressDialog(this);

        storageReference = FirebaseStorage.getInstance().getReference();

        binding.imageIdentificacion.setOnClickListener(new View.OnClickListener() {
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

        // Google Connection
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        gsc = GoogleSignIn.getClient(this, gso);
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
        HashMap<String, Object> userObject = new HashMap<>();
        List<String> personDocuments = new ArrayList<>();
        personDocuments.add("");
        Log.d("PERSON", personDocuments.toArray().toString());
        userObject.put(Person.IDPHOTOS_KEY, personDocuments.toArray());
        userDocRef.update(userObject);
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
        userDocRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Person persona = documentSnapshot.toObject(Person.class);
                            if(
                                    persona.getNombre().isEmpty() ||
                                            persona.getApellido().isEmpty() ||
                                            persona.getCedula() == null ||
                                            persona.getCelular() == null
                            ) {
                                setFields(persona);
                            } else {
                                MainActivity();
                            }
                        } else {
                            Log.w("USER", "NO EXISTE");
                        }
                    }
                });
    }

    /**
     * Selecciona el tipo "Conductor"
     * @param view La vista
     */
    public void onSelectDriver(View view){
        Log.d("BOTON", "SE HACE");
        binding.driverSectionLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Selecciona el tipo "Pasajero"
     * @param view La vista
     */
    public void onSelectPassenger(View view){
        binding.driverSectionLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("IMAGEN", "requestCode - RESULT_OK: " + requestCode + " " + RESULT_OK);
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
        progressDialog.setMessage("Actualizando foto");
        progressDialog.show();
        String photoRoute = storagePath + "" + photo + "" + userId + "" + mAuth.getUid();
        StorageReference reference = storageReference.child(photoRoute);
        reference.putFile(photoUrl).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> taskSnapshot) {
                if(taskSnapshot.isSuccessful()){
                    Task<Uri> uriTask = taskSnapshot.getResult().getStorage().getDownloadUrl();
                    while(!uriTask.isSuccessful()){
                        if(uriTask.isSuccessful()){
                            uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String downloadUri = uri.toString();
                                    HashMap<String, Object> userObject = new HashMap<>();
                                    Log.d("URI DOWNLOAD", downloadUri);
                                    userObject.put(Person.IDPHOTOS_KEY, Arrays.asList(downloadUri));
                                    userDocRef.update(userObject);
                                    Toast.makeText(RegisterActivity.this, "Foto actualizada", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    getImage();
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Error al cargar foto", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    /**
     * Pone todos los campos de la base de datos
     * @param person El documento de la base de datos del usuario
     */
    private void setFields(Person person){
        binding.signUpEmail.setText(person.getEmail() != null ? person.getEmail() : email);
        binding.signupNameInput.setText(person.getNombre());
        binding.signupLastNameInput.setText(person.getApellido());
        binding.signupCellphoneInput.setText(person.getDireccion());
        binding.signupCellphoneInput.setText(person.getCelular() != null ? person.getCelular().toString() : "");
        binding.signupIdInput.setText(person.getCedula() != null ? person.getCedula().toString() : "");
        getImage();
    }

    /**
     * Muestra la imagen del documento de identidad
     */
    private void getImage(){
        userDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                List<String> documents = (List<String>) documentSnapshot.get(Person.IDPHOTOS_KEY);
                if(documents.size() > 0){
                    String documentPhoto = documents.get(0);
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
        });
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
            Log.w("ERROR", "No se puede continuar");
            Toast.makeText(this, "No se puede continuar, faltan campos por llenar", Toast.LENGTH_SHORT).show();
        } else {
            Map<String, Object> userData = new HashMap<String, Object>();
            userData.put(Person.MAIL_KEY, email);
            userData.put(Person.NAME_KEY, nombre);
            userData.put(Person.LASTNAME_KEY, apellido);
            userData.put(Person.ADDRESS_KEY, direccion);
            userData.put(Person.CELLPHONE_KEY, cellphone);
            userData.put(Person.ID_KEY, identificacion);

            db.collection("users").document(email).update(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    private void MainActivity(){
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}