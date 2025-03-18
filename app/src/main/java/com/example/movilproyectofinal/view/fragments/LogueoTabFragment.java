package com.example.movilproyectofinal.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.example.movilproyectofinal.R;
import com.example.movilproyectofinal.databinding.FragmentLogueoTabBinding;
import com.example.movilproyectofinal.model.User;
import com.example.movilproyectofinal.util.Validaciones;
import com.example.movilproyectofinal.view.Login;
import com.example.movilproyectofinal.viewModel.RegisterViewModel;
import com.google.android.material.snackbar.Snackbar;


public class LogueoTabFragment extends Fragment {


    private FragmentLogueoTabBinding binding;
    private RegisterViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLogueoTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Creamos vm
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        //Observa los resultados del registro.Si es exitoso o fallido, se ejecutará este bloque de código.
        viewModel.getRegisterResult().observe(getViewLifecycleOwner(), result -> {//result contendrá el objectId del usuario registrado si tuvo éxito, o null si falló.
            // Ocultar ProgressBar y habilitar el botón
            binding.progressBar.setVisibility(View.GONE);
            binding.btRegistrar.setEnabled(true);

            if (result != null) {
                Snackbar.make(binding.getRoot(), "🎉 Registro exitoso, ¡Bienvenido!", Snackbar.LENGTH_INDEFINITE)//EL CARTELITO SE MUESTRA HASTA QUE EL USUARIO INTERACTUE
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.primary1))
                        .setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                        .show();

                // Cambiar al fragmento de inicio de sesión después del registro exitoso
                irAlFragmentDeInicioSesion();
            } else {
                Snackbar.make(binding.getRoot(), "❌ Error en el registro, intenta nuevamente", Snackbar.LENGTH_INDEFINITE)
                        .setBackgroundTint(ContextCompat.getColor(requireContext(), android.R.color.holo_red_light))
                        .setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                        .show();
            }

        });


        manejarEventos();
    }

    private void manejarEventos() {

        binding.btRegistrar.setOnClickListener(v -> realizarRegistro());
    }

    private void realizarRegistro() {
        String usuario = binding.itUsuario.getText().toString().trim();
        String email = binding.itEmail.getText().toString().trim();
        String pass = binding.itPassword.getText().toString().trim();
        String pass1 = binding.itPassword1.getText().toString().trim();

        // Validaciones
        if (!Validaciones.validarTexto(usuario)) {
            showToast("El Usuario debe contener al menos 3 caracteres ");
            return;
        }
        if (!Validaciones.validarMail(email)) {
            showToast("El correo no es válido");
            return;
        }
        String passError = Validaciones.validarPass(pass, pass1);
        if (passError != null) {
            showToast(passError);
            return;
        }

        // Mostrar ProgressBar y desactivar el botón para que no puedan tocarlo varias veces
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.btRegistrar.setEnabled(false);

        User user = new User();
        user.setUsername(usuario);
        user.setEmail(email);
        user.setPassword(pass);

        Log.d("LogueoTabFragment", "Registrando usuario: " + usuario + ", Email: " + email);

        viewModel.register(user);//registra el usuario creado
    }


    private void irAlFragmentDeInicioSesion() {
        //getActivity() devuelve la Activity a la que está asociado el Fragment.
        //instanceof verifica si getActivity() es una instancia de Login
        if (getActivity() instanceof Login) {
        //    Como sabemos  getActivity()  es de tipo Login (gracias al instanceof), lo convertimos (cast) a Login
        //esto nos permite acceder a los métodos específicos de Login, como cambiarATabInicioSesion()
            ((Login) getActivity()).cambiarATabInicioSesion();
        }

        //mas claro: Si la Activity de este Fragment es Login, entonces ejecuta el método cambiarATabInicioSesion().
    }




    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {// esto no lo uso ver ... deberia ver como lo uso la profe!
        super.onDestroyView();
        binding = null; // Evitar memory leaks
    }
}