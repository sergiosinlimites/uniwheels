package com.uniwheelsapp.uniwheelsapp.usecases.requests;

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
import com.google.firebase.firestore.QuerySnapshot;
import com.uniwheelsapp.uniwheelsapp.models.PasajeroViaje;
import com.uniwheelsapp.uniwheelsapp.models.Viaje;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestTravelsViewModel extends AndroidViewModel {

    private FirebaseDBService dbService;
    private MutableLiveData<ArrayList<Viaje>> viajes;
    private DocumentReference viajeDocRef;

    public RequestTravelsViewModel(@NonNull Application application) {
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
                        if(viajeArreglado.getPasajeros() != null){
                            for(PasajeroViaje pasajeroViaje : viajeArreglado.getPasajeros()){
                                if(pasajeroViaje.getEstadoSolicitud().equals("EN REVISIÓN")){
                                    Viaje viajeModificado = viajeArreglado;
                                    viajeModificado.setPasajero(pasajeroViaje);
                                    viajeModificado.setDocumentId(documento.getId());
                                    viajesArreglados.add(viajeModificado);
                                }
                            }
                        }
                    }
                    viajes.postValue(viajesArreglados);
                    Log.d("Viajes arreglados", String.valueOf(viajesArreglados.size()));
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), "Hubo un error al obtener los viajes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void setTravelDocument(String document){
        viajeDocRef = dbService.setReference("viajes", document);
    }

    public void modificarSolicitud(Viaje viaje, String estado) {
        ArrayList<PasajeroViaje> pasajeros;
        pasajeros = viaje.getPasajeros();
        PasajeroViaje pasajeroViaje = viaje.getPasajero();
        pasajeroViaje.setEstadoSolicitud(estado);
        int indice = pasajeros.indexOf(pasajeroViaje);
        pasajeros.set(indice, pasajeroViaje);
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("pasajeros", pasajeros);
        setTravelDocument(viaje.getDocumentId());
        viajeDocRef.update(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplication().getApplicationContext(), "Solicitud modificada exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), "Hubo un problema al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
