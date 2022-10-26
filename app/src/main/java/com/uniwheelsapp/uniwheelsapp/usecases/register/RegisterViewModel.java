package com.uniwheelsapp.uniwheelsapp.usecases.register;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegisterViewModel extends ViewModel {
    // lectura y escritura
    private MutableLiveData<String> resultado;

     public RegisterViewModel(){
         resultado = new MutableLiveData<>();
     }

     // solo lectura
     public LiveData<String> getResultado(){
        return resultado;
     }
}
