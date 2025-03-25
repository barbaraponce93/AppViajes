package com.example.movilproyectofinal.view;

import android.app.Application;

import com.example.movilproyectofinal.R;
import com.example.movilproyectofinal.model.Post;
import com.example.movilproyectofinal.model.User;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);//Esta línea habilita la función de almacén de datos local proporcionada por ParseSDK.



        /*En Parse, la clase ParseObject es la que representa un objeto almacenado en la base de datos de Parse.
         Para poder interactuar con una clase personalizada en la base de datos, debes registrarla como una subclase de ParseObject.
        El método registerSubclass() se usa para registrar una clase personalizada (en tu caso, Post,User) como una subclase de ParseObject,
       de modo que Parse pueda reconocerla como un objeto que interactúa con la base de datos de Parse.
        Cuando llamas a ParseObject.registerSubclass(Post.class);, le estás indicando a Parse que la clase Post debe ser tratada como
        una representación de un objeto almacenado en la base de datos, y que sus instancias deben poder ser guardadas, consultadas,
         actualizadas o eliminadas como cualquier otro objeto de Parse.
        ¿Por qué es necesario?
        Si no registras tu clase personalizada como una subclase de ParseObject, Parse no podrá reconocerla y no podrás
         realizar operaciones como guardar, obtener o eliminar objetos de esa clase en la base de datos.
          Este registro es necesario para que Parse pueda mapear correctamente las propiedades de tu clase con los datos
           almacenados en su base de datos.*/

        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(Post.class);



        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        ) ;


        ParseACL defaultACL= new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(false);// lo paso a falso para que solo el usuario pueda modificar
        ParseACL.setDefaultACL(defaultACL,true);




    }



}