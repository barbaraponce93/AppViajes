package com.example.movilproyectofinal.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.movilproyectofinal.R;


import com.example.movilproyectofinal.databinding.ActivityHomeBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.movilproyectofinal.view.fragments.BusquedaFragment;
import com.example.movilproyectofinal.view.fragments.ChatFragment;
import com.example.movilproyectofinal.view.fragments.HomeFragment;
import com.example.movilproyectofinal.view.fragments.PerfilFragment;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar el BottomNavigationView
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    openFragment(HomeFragment.newInstance("",""));
                } else if (item.getItemId() == R.id.nav_buscar) {
                    openFragment(new BusquedaFragment());
                } else if (item.getItemId() == R.id.nav_chat) {
                    openFragment(new ChatFragment());
                } else if (item.getItemId() == R.id.nav_perfil) {
                    openFragment(new PerfilFragment());
                }
                return true;
            }
        });

        // Abrir el fragmento inicial
        openFragment(HomeFragment.newInstance("",""));


        binding.buttonFloating.setOnClickListener(v -> {
            Intent intent = new Intent(this, PostActivity.class);
            startActivity(intent);
        });

    }


    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


    public void hideProgressBar() {
        View progressBarLayout = findViewById(R.id.progress_layout);
        if (progressBarLayout != null) {
            progressBarLayout.setVisibility(View.GONE);
        }
    }




}