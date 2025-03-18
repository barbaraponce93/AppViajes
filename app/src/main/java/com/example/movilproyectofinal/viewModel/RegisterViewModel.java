package com.example.movilproyectofinal.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.movilproyectofinal.model.User;
import com.example.movilproyectofinal.providers.AuthProvider;

public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<String> registerResult = new MutableLiveData<>();
    private final AuthProvider authProvider;


    public RegisterViewModel() {
        this.authProvider = new AuthProvider();
    }


    public LiveData<String> getRegisterResult() {
        return registerResult;
    }


    public void register(User user) {
        //Llamo a signUp(user) del AuthProvider que se encargará de enviar el usuario a Parse.
        LiveData<String> result = authProvider.signUp(user);// este live data notificará cuando haya un resultado (ya sea el ID del usuario registrado o null si hubo un error).


        result.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String objectId) {
                if (objectId != null) {

                    registerResult.setValue(objectId);
                    Log.d("RegisterViewModel", "Usuario registrado con ID: " + objectId);
                } else {
                    // Registration failed
                    registerResult.setValue(null);
                    Log.e("RegisterViewModel", "Error durante el registro.");
                }

                result.removeObserver(this);//eliminamos en observer una vez que obtenemos el resultado
            }
        });
    }
}







