package com.example.movilproyectofinal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.movilproyectofinal.providers.AuthProvider;

public class AuthViewModel extends ViewModel {
    private final AuthProvider authProvider;

    public AuthViewModel() {
        this.authProvider = new AuthProvider();
    }

    public LiveData<Boolean> logout() {
        return authProvider.logout();
    }}
