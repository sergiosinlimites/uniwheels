package com.uniwheelsapp.uniwheelsapp.usecases.home;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseAuthService;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

public class MainViewModel extends AndroidViewModel {

    private FirebaseAuthService authService;
    private FirebaseDBService dbService;
    private DocumentReference userDocRef = null;

    private MutableLiveData<GoogleSignInAccount> account;
    private MutableLiveData<Boolean> loggedStatus;
    private MutableLiveData<Person> personMutableLiveData;

    public MainViewModel(@NonNull Application application) {
        super(application);
        authService = new FirebaseAuthService(application);
        dbService = new FirebaseDBService();
        account = authService.getAccountMutableLiveData();
        loggedStatus = authService.getLoggedStatus();
        personMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<GoogleSignInAccount> getAccount() {
        return account;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }

    public MutableLiveData<Person> getPersonMutableLiveData() {
        return personMutableLiveData;
    }

    public void setUserDocument(String document){
        userDocRef = dbService.setReference("users", document);
    }

    public Task<DocumentSnapshot> getUserData(String document){
        if(userDocRef == null){
            setUserDocument(document);
        }
        return dbService.getData(userDocRef);
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
}
