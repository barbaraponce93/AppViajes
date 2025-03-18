package com.example.movilproyectofinal.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.movilproyectofinal.databinding.FragmentInicioSesionBinding;
import com.example.movilproyectofinal.view.HomeActivity;
import com.example.movilproyectofinal.viewModel.LoginViewModel;
import com.example.movilproyectofinal.util.Validaciones; // Si tienes tu clase de validaciones

public class InicioSesionFragment extends Fragment {
    private FragmentInicioSesionBinding binding;
    private LoginViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentInicioSesionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    //onViewCreated se llama después de que la vista del fragmento ha sido creada,
    // pero antes de que el fragmento sea visible para el usuario.
    // se usa para: -nicializar variables y objetos relacionados con la vista.
    //- Configurar listeners -Observar LiveData y manejar datos dinámicos. -Modificar la UI antes de que se muestre al usuario.

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //intancio el vm
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        // Listener para iniciar sesión
        binding.btLogin.setOnClickListener(v -> manejarEventos());
    }

    private void manejarEventos() {
        String email = obtenerTextoSeguro(binding.itUsuario);
        String pass = obtenerTextoSeguro(binding.itPassword);

        if (!Validaciones.validarMail(email)) {
            showToast("Email incorrecto");
            return;
        }

        if (!Validaciones.controlarPasword(pass)) {
            showToast("Password incorrecto");
            return;
        }

        //Si las validaciones son correctas, salto a viewModel.login(email, pass)
        //llamo al vm que devuelve un LiveData<String> con el user_id si la autenticación es exitosa.
        viewModel.login(email, pass).observe(getViewLifecycleOwner(), user_id -> {
            if (user_id != null) {
                //Si el usuario ha iniciado sesión correctamente se abre el HomeActivity
                Intent intent = new Intent(requireContext(), HomeActivity.class);
                startActivity(intent);
            } else {
                showToast("" + "Login fallido");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        limpiarCampos();
    }

    private void limpiarCampos() {
        if (binding != null) {
            binding.itUsuario.setText("");
            binding.itPassword.setText("");
        }
    }

    private String obtenerTextoSeguro(EditText editText) {

        if (editText == null) {
            return "";
        }
        return editText.getText().toString().trim();
    }
}
