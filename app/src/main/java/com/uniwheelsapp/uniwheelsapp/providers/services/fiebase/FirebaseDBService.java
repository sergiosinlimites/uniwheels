package com.uniwheelsapp.uniwheelsapp.providers.services.fiebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FirebaseDBService extends Service {
    public FirebaseDBService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}