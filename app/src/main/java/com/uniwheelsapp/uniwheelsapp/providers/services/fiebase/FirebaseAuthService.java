package com.uniwheelsapp.uniwheelsapp.providers.services.fiebase;

import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.uniwheelsapp.uniwheelsapp.R;

public class FirebaseAuthService extends Service {
    private Application application;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private GoogleSignInAccount account;

    private MutableLiveData<GoogleSignInAccount> accountMutableLiveData;
    private MutableLiveData<Boolean> loggedStatus;

    public MutableLiveData<GoogleSignInAccount> getAccountMutableLiveData() {
        return accountMutableLiveData;
    }

    public MutableLiveData<Boolean> getLoggedStatus() {
        return loggedStatus;
    }

    public FirebaseAuthService(Application application) {
        this.application = application;
        accountMutableLiveData = new MutableLiveData<>();
        loggedStatus = new MutableLiveData<>();
        gso = new GoogleSignInOptions.Builder()
                .requestIdToken(application.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(application.getApplicationContext(), gso);
        account = GoogleSignIn.getLastSignedInAccount(application.getApplicationContext());
        if(account != null){
            accountMutableLiveData.postValue(account);
        }
    }

    public Intent signIn(){
        return gsc.getSignInIntent();
    }

    public void signAccountFromIntent(Intent data){
        try {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if(account != null){
                AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d("TASK REGISTER", task.getResult().getUser().toString());
                            loggedStatus.postValue(true);
                        } else {
                            Toast.makeText(application.getApplicationContext(), "Hubo un error en la autenticación", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (ApiException e) {
            e.printStackTrace();
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}