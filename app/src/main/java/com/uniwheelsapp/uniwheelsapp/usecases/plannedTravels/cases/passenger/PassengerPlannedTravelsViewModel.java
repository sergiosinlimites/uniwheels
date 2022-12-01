package com.uniwheelsapp.uniwheelsapp.usecases.plannedTravels.cases.passenger;

import android.app.Application;
import android.app.Person;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.uniwheelsapp.uniwheelsapp.adapters.PlannedTravelsPassengerAdapter;
import com.uniwheelsapp.uniwheelsapp.databinding.FragmentPassengerPlannedTravelsBinding;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

import java.util.ArrayList;
import java.util.List;

public class PassengerPlannedTravelsViewModel extends AndroidViewModel {

    private FirebaseDBService dbService;

    public PassengerPlannedTravelsViewModel(@NonNull Application application) {
        super(application);
        dbService = new FirebaseDBService();
    }

    public void getTravelsByPassenger(PlannedTravelsPassengerAdapter adapter, String email) {
        dbService.searchByArrayItem("viajes", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Viaje> viajes = new ArrayList<>();
                    List<DocumentSnapshot> documentosViajes = task.getResult().getDocuments();

                    for(DocumentSnapshot documento : documentosViajes) {
                        Viaje viaje = documento.toObject(Viaje.class);
                        viaje.setDocumentId(documento.getId());
                        viajes.add(viaje);
                    }

                    adapter.updateData(viajes);

                } else {
                    Toast.makeText(getApplication().getApplicationContext(), "Hubo un error al obtener los viajes", Toast.LENGTH_SHORT).show();
                    Log.d("Error", "No se obtuvieron los viajes");
                }
            }
        });
    }
}