package com.example.movilproyectofinal.providers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movilproyectofinal.model.User;
import com.parse.ParseUser;

public class AuthProvider {

    public AuthProvider() {

    }
    public LiveData<String> signIn(String email, String password) {
        MutableLiveData<String> authResult = new MutableLiveData<>();

        //se usa ParseUser.logInInBackground para autenticar en segundo plano.
        ParseUser.logInInBackground(email, password, (user, e) -> {
            if (e == null) {
                // Login exitoso, no hubieron errores
                authResult.setValue(user.getObjectId());//Se obtiene el ObjectId del usuario y se guarda en authResult.
                Log.d("AuthProvider", "Usuario autenticado exitosamente: " + user.getObjectId());
            } else {
                // Error en el login
                Log.e("AuthProvider", "Error en inicio de sesión: ", e);
                authResult.setValue(null);
            }
        });
        //Se devuelve el LiveData con el resultado que se va a observar en LoginViewModel
        return authResult;
    }
    // Registro con Parse
    public LiveData<String> signUp(User user) {
        //Crea un LiveData que guardará el resultado del registro
      MutableLiveData<String> authResult = new MutableLiveData<>();//

        //COMPROBAMOS QUE NO HAYAN DATOS NULOS
  // ACA LA COMPROBACION TAMBIEN ES REBUNDANTE PORQUE EN LogueoTabFragment SE HACEN LAS VALIDACIONES
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            Log.e("AuthProvider", "Uno o más valores son nulos: " +
                    "Username=" + user.getUsername() + ", " +
                    "Password=" + user.getPassword() + ", " +
                    "Email=" + user.getEmail());
            authResult.setValue(null);
            return authResult;
        }
        ParseUser parseUser = ParseUser.create(ParseUser.class); //


        //enviamos el usuario en el servidor en segundo plano (es asincrono) para no bloquear la UI
        parseUser.signUpInBackground(e -> {
            if (e == null) {// (e error: si el error es null,salio todo bien
                // Registro exitoso
                authResult.setValue(parseUser.getObjectId());//Si el registro fue exitoso, guarda el objectId del usuario en el LiveData authResult
                Log.d("AuthProvider", "Usuario registrado exitosamente: " + parseUser.getObjectId());
            } else {
                //si hubo error se guarda null
                Log.e("AuthProvider", "Error en registro: ", e);
                authResult.setValue(null);
            }
        });
        return authResult;
    }



}
