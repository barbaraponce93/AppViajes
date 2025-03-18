package com.example.movilproyectofinal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movilproyectofinal.providers.AuthProvider;

public class LoginViewModel extends ViewModel {
    public final AuthProvider authProvider;
    public LoginViewModel(){

        authProvider=new AuthProvider();
    }

    public LiveData<String> login(String email, String password) {
        MutableLiveData<String> loginResult = new MutableLiveData<>();
        //LLAMO AL AUTHprovider  y observo su resultado
        authProvider.signIn(email, password).observeForever(userId -> {
            //setteo el userId con el resultado que se observa en el authProvider.
            loginResult.setValue(userId);
        });
        return loginResult;// //actualizo el  loginResult segun el resultado
    }
}
