package com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.uniwheelsapp.uniwheelsapp.models.Person;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

import java.util.ArrayList;
import java.util.List;

public class PlannedTravelsViewModel extends AndroidViewModel {

    private FirebaseDBService dbService;
    private MutableLiveData<ArrayList<Viaje>> viajes;

    public PlannedTravelsViewModel(@NonNull Application application) {
        super(application);

        dbService = new FirebaseDBService();
        // dbService.setReference("viajes");
    }


    public void getTravelsByPerson(Person person){
//        dbService.searchByField("viajes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    ArrayList<Viaje> viajes = new ArrayList<>();
//
//                    List<DocumentSnapshot> documentosViajes = task.getResult().getDocuments();
//
//                    for(DocumentSnapshot documento : documentosViajes){
//                        Viaje viaje = documento.toObject(Viaje.class);
//                        viajes.add(viaje);
//                    }
//
//                } else {
//
//                }
//            }
//        });
    }
}
