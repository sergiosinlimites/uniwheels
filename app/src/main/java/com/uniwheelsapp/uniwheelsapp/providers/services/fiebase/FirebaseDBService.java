package com.uniwheelsapp.uniwheelsapp.providers.services.fiebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Map;

public class FirebaseDBService extends Service {

    // Firebase FireStore
    private FirebaseFirestore db;

    public FirebaseDBService(){
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public DocumentReference setReference(String path, String document){
        return db.collection(path).document(document);
    }

    public Query searchByField(String path, String fieldName, String fieldValue){
        return db.collection(path)
            .whereEqualTo(fieldName, fieldValue);
    }

    public Task<DocumentSnapshot> getData(DocumentReference documentReference){
        return documentReference.get();
    }

    public Task<Void> updateData(DocumentReference documentReference, Map<String, Object> data){
        return documentReference.update(data);
    }

}