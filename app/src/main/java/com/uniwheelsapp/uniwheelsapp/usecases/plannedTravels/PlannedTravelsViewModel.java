package com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

import java.util.ArrayList;
import java.util.List;

public class PlannedTravelsViewModel extends AndroidViewModel {

    private FirebaseDBService dbService;
    private DocumentReference userDocRef = null;
    private MutableLiveData<Person> personMutableLiveData;

    public MutableLiveData<Person> getPersonMutableLiveData() {
        return personMutableLiveData;
    }

    public PlannedTravelsViewModel(@NonNull Application application) {
        super(application);

        dbService = new FirebaseDBService();
        personMutableLiveData = new MutableLiveData<>();
    }

    public void setUserDocument(String document){
        userDocRef = dbService.setReference("users", document);
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
