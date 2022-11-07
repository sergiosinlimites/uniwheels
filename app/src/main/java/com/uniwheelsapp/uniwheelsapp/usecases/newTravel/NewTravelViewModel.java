package com.uniwheelsapp.uniwheelsapp.usecases.newTravel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

public class NewTravelViewModel extends AndroidViewModel {

    private FirebaseDBService firebaseDBService;
    private MutableLiveData<Boolean> successfulCreation;

    public NewTravelViewModel(@NonNull Application application) {
        super(application);
        firebaseDBService = new FirebaseDBService();
        successfulCreation = new MutableLiveData<>();
    }

    public MutableLiveData<Boolean> getSuccessfulCreation() {
        return successfulCreation;
    }

    public void setSuccessfulCreation(MutableLiveData<Boolean> successfulCreation) {
        this.successfulCreation = successfulCreation;
    }

    public void createTravel(Viaje viaje){
        this.firebaseDBService.createDataWithoutId("viajes", viaje).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    successfulCreation.postValue(true);
                } else {
                    successfulCreation.postValue(false);
                }
            }
        });
    }
}
