package com.uniwheelsapp.uniwheelsapp.usecases.home.cases.driver;

import android.app.Application;
import android.app.Person;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseAuthService;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseDBService;

public class DriverHomeViewModel extends AndroidViewModel {
    private FirebaseAuthService authService;
    private FirebaseDBService dbService;
    private MutableLiveData<GoogleSignInAccount> account;
    private MutableLiveData<Person> personMutableLiveData;

    public DriverHomeViewModel(@NonNull Application application) {
        super(application);
        dbService = new FirebaseDBService();
        authService = new FirebaseAuthService(application);
        account = authService.getAccountMutableLiveData();
        personMutableLiveData = new MutableLiveData<>();
    }
}
