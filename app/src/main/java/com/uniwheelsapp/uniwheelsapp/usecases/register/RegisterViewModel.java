package com.uniwheelsapp.uniwheelsapp.usecases.register;

import android.app.Application;
import android.app.ProgressDialog;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseAuthService;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RegisterViewModel extends AndroidViewModel {
    private FirebaseAuthService authService;
    private FirebaseDBService dbService;
    private DocumentReference userDocRef = null;
    // Live Data
    private MutableLiveData<GoogleSignInAccount> accountData;
    private MutableLiveData<Boolean> loggedStatus;
    private MutableLiveData<Person> personMutableLiveData;
    private MutableLiveData<Boolean> isLoading;

    // Utils


    // Firebase Auth
    private String userId;

    // Firebase Storage
    private StorageReference storageReference;
    private String storagePath = "documents/*";
    private String photo = "photo";

    public MutableLiveData<GoogleSignInAccount> getAccountData() {
        return accountData;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<Person> getPerson() {
        return personMutableLiveData;
    }

    public RegisterViewModel(@NonNull Application application){
         super(application);
         authService = new FirebaseAuthService(application);
         dbService = new FirebaseDBService();

         accountData = authService.getAccountMutableLiveData();
         loggedStatus = authService.getLoggedStatus();
         personMutableLiveData = new MutableLiveData<>();
         isLoading = new MutableLiveData<>();

         storageReference = FirebaseStorage.getInstance().getReference();
    }

    public void setUserDocument(String document){
        userDocRef = dbService.setReference("users", document);
    }

    public Task<DocumentSnapshot> getUserData(@Nullable String document){
        if(userDocRef == null){
            if(document == null){
                return null;
            }
            setUserDocument(document);
        }
        return dbService.getData(userDocRef);
    }

    public Task<Void> updateUserDocument(String document, Map<String, Object> userObject){
        if (userObject == null){
            setUserDocument(document);
        }
        return dbService.updateData(userDocRef, userObject);
    }

    public void searchInDB(String document){
        getUserData(document).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Person persona = documentSnapshot.toObject(Person.class);
                    personMutableLiveData.postValue(persona);
                } else {
                    Log.w("USER", "NO EXISTE");
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
                                    userObject.put(Person.IDPHOTOS_KEY, Arrays.asList(downloadUri));
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

}
