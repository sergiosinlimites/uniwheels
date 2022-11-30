package com.uniwheelsapp.uniwheelsapp.usecases.profile;

import android.app.Application;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseAuthService;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProfileViewModel extends AndroidViewModel {
    private FirebaseAuthService authService;
    private FirebaseDBService dbService;
    private DocumentReference userDocRef = null;
    private MutableLiveData<Person> personMutableLiveData;
    private MutableLiveData<GoogleSignInAccount> accountData;
    private MutableLiveData<Boolean> isLoading;

    // Firebase Auth
    private String userId;

    // Firebase Storage
    private StorageReference storageReference;
    private String storagePath = "documents/*";
    private String photo = "photo";

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        authService = new FirebaseAuthService(application);
        dbService = new FirebaseDBService();

        accountData = authService.getAccountMutableLiveData();
        isLoading = new MutableLiveData<>();
        personMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Person> getPersonMutableLiveData() {
        return personMutableLiveData;
    }

    public MutableLiveData<GoogleSignInAccount> getAccountData() {
        return accountData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void setUserDocument(String document){
        userDocRef = dbService.setReference("users", document);
    }


    public void setInactive(Person person){
        setUserDocument(person.getEmail());
        Map<String, Object> cambios = new HashMap<>();
        cambios.put("activo", false);
        userDocRef.update(cambios).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplication().getApplicationContext(), "Usuario eliminado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), "Hubo un error al actualizar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void listenForChanges(String document){
        setUserDocument(document);
        userDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("ERROR OBTENIENDO DATOS", error.getMessage());
                    return;
                }
                if(documentSnapshot != null && documentSnapshot.exists()){
                    Person persona = documentSnapshot.toObject(Person.class);
                    personMutableLiveData.postValue(persona);
                }
            }
        });
    }

    /**
     * Sube la foto a Firebase Storage
     * @param photoUrl La url del documento
     */
    public void updatePhoto(Uri photoUrl){
        isLoading.postValue(true);
        userId = accountData.getValue().getId();
        String photoRoute = storagePath + "" + photo + "" + userId.toString() + "" + authService.getUid();
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
                                    userObject.put(Person.PHOTO_KEY, Arrays.asList(downloadUri));
                                    updateUserDocument(personMutableLiveData.getValue().getEmail(), userObject);
                                    Toast.makeText(getApplication(), "Foto actualizada", Toast.LENGTH_SHORT).show();
                                    isLoading.postValue(false);
                                }
                            });
                        }
                    }
                } else {
                    Toast.makeText(getApplication(), "Error al cargar foto", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    public Task<Void> updateUserDocument(String document, Map<String, Object> userObject){
        if (userObject == null){
            setUserDocument(document);
        }
        return dbService.updateData(userDocRef, userObject);
    }

}
