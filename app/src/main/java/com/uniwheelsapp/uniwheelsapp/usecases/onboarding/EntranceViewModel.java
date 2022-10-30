package com.uniwheelsapp.uniwheelsapp.usecases.onboarding;

import android.app.Application;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.uniwheelsapp.uniwheelsapp.providers.services.fiebase.FirebaseAuthService;

public class EntranceViewModel extends AndroidViewModel {

    private FirebaseAuthService authService;
    private MutableLiveData<GoogleSignInAccount> accountData;
    private MutableLiveData<Boolean> loggedStatus;

    public MutableLiveData<GoogleSignInAccount> getAccountData() {
        return accountData;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }

    public EntranceViewModel(@NonNull Application application) {
        super(application);
        authService = new FirebaseAuthService(application);
        accountData = authService.getAccountMutableLiveData();
        loggedStatus = authService.getLoggedStatus();
    }

    public Intent entrance(){
        return authService.signIn();
    }

    public void updateAccount(Intent intent){
        authService.signAccountFromIntent(intent);
    }
}
