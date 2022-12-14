package com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.cases.driver;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.uniwheelsapp.uniwheelsapp.adapters.PlannedTravelsAdapter;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverPlannedTravelsViewModel extends AndroidViewModel {

    private FirebaseDBService dbService;

    public DriverPlannedTravelsViewModel(@NonNull Application application) {
        super(application);
        dbService = new FirebaseDBService();
        
    }

    public void getTravelsByDriver(PlannedTravelsAdapter adapter, String driverEmail){
        dbService.searchByField("viajes", "conductor.correo", driverEmail).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Viaje> viajes = new ArrayList<>();
                    List<DocumentSnapshot> documentosViajes = task.getResult().getDocuments();

                    for(DocumentSnapshot documento : documentosViajes){
                        Viaje viaje = documento.toObject(Viaje.class);
                        viaje.setDocumentId(documento.getId());

                        Log.d("estado"+viaje.getDocumentId(), viaje.getEstadoViaje());
                        if(!viaje.getEstadoViaje().equals("CANCELADO")){
                            viajes.add(viaje);
                        }
                    }
                    adapter.updateData(viajes);
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), "Hubo un error al obtener los viajes", Toast.LENGTH_SHORT).show();
                    Log.d("Error", "No se obtuvieron los viajes");
                }
            }
        });
    }

    public void cancelTravel(String documentReference){
        DocumentReference docRef = dbService.setReference("viajes", documentReference);
        Map<String, Object> data = new HashMap<>();
        data.put("estadoViaje", "CANCELADO");
        dbService.updateData(docRef, data);
    }
}