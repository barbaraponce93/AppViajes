package com.example.movilproyectofinal.model;

import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("_User")
public class User extends ParseUser {

    public User() {
        // Constructor vacío necesario para Parse
    }

    // Getter y setter para "redSocial"
    public String getRedSocial() {

        return getString("redSocial");
    }

    public void setRedSocial(String redSocial) {
        if (redSocial != null) {
            put("redSocial", redSocial);
        }
    }

    // Getter y setter para "fotoperfil"
    public String getFotoperfil() {

        return getString("fotoperfil");
    }

    public void setFotoperfil(String fotoperfil) {
        if (fotoperfil != null) {
            put("fotoperfil", fotoperfil);
        }
    }

    // Getter y setter para "username"
    // Estos métodos son solo para establecer y obtener campos en la base de datos de Parse.
    public String getUsername() {

        return getString("username");
    }



    public void setUsername(String username) {

        put("username", username);
    }

    // Getter y setter para "email"
    public String getEmail() {

        return getString("email");
    }

    public void setEmail(String email) {
        if (email != null) {
            put("email", email);
        } else {
            Log.w("User", "El correo electrónico es nulo.");
        }
    }

    // Getter y setter para "password"
    public String getPassword() {
        return getString("password");
    }

    public void setPassword(String password) {
        put("password", password);
    }

    // Getter para "id" (no necesitas un setter para "id" porque Parse lo genera automáticamente)
    public String getId() {
        return getObjectId();
    }
}
