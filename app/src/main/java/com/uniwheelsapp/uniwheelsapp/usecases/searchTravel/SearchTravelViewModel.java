package com.uniwheelsapp.uniwheelsapp.usecases.searchTravel;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class SearchTravelViewModel extends AndroidViewModel {

    private FirebaseDBService dbService;
    private MutableLiveData<ArrayList<Viaje>> viajes;
    private DocumentReference viajeDocRef;

    public SearchTravelViewModel(@NonNull Application application) {
        super(application);
        dbService = new FirebaseDBService();
        viajes = new MutableLiveData<>();
    }

    public MutableLiveData<ArrayList<Viaje>> getViajes() {
        return viajes;
    }

    public void getTravelsFromDate(){
        dbService.searchTravelsWithNoDates().get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Viaje> viajesArreglados = new ArrayList<>();
                    List<DocumentSnapshot> viajesEncontrados = task.getResult().getDocuments();
                    for(DocumentSnapshot documento : viajesEncontrados){
                        Viaje viajeArreglado = documento.toObject(Viaje.class);
                        viajeArreglado.setDocumentId(documento.getId());
                        viajesArreglados.add(viajeArreglado);
                    }
                    viajes.postValue(viajesArreglados);
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), "Hubo un error al obtener los viajes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setTravelDocument(String document){
        viajeDocRef = dbService.setReference("viajes", document);
    }

    public void updatePassengers(String document, Map<String, Object> updateData) {
        setTravelDocument(document);
        Log.d("Documento", document);
        viajeDocRef.update(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplication().getApplicationContext(), "Se ha mandado la solicitud correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), "Hubo un error al enviar la solicitud", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
