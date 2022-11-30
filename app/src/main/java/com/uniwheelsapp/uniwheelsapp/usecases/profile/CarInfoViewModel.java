package com.uniwheelsapp.uniwheelsapp.usecases.profile;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.uniwheelsapp.uniwheelsapp.models.Vehiculo;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

import java.util.HashMap;
import java.util.Map;

public class CarInfoViewModel extends AndroidViewModel {

    private FirebaseDBService dbService;
    private DocumentReference userDocRef = null;

    public CarInfoViewModel(@NonNull Application application) {
        super(application);
        dbService = new FirebaseDBService();
    }

    public void setUserDocument(String document){
        userDocRef = dbService.setReference("users", document);
    }

    public void updateVehicle(String document, Vehiculo vehiculo){
        setUserDocument(document);
        Log.d("VEHICULO","Actualizando");
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("vehiculo", vehiculo);
        userDocRef.update(updateData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplication().getApplicationContext(), "Se ha actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplication().getApplicationContext(), "Ha ocurrido un error al actualizar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
