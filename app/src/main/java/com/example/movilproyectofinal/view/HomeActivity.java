package com.example.movilproyectofinal.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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


        //progress bar
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewProgresBar = layoutInflater.inflate(R.layout.progress_layout, binding.contProgress, false);
        binding.contProgress.addView(viewProgresBar);

        // Cargar fragmento inicial
        loadInitialFragment();

        String fragmentToLoad = getIntent().getStringExtra("FRAGMENT_TO_LOAD");
        Log.d("HomeActivity", "onCreate() called");
        Log.d("HomeActivity", "fragmentToLoad: " + fragmentToLoad);
        if ("PROFILE".equals(fragmentToLoad)) {
            Log.d("HomeActivity", "Loading PerfilFragment");
            loadFragment(new PerfilFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_perfil); // Actualizar el BottomNavigationView
        } else if ("HOME".equals(fragmentToLoad)) {
            Log.d("HomeActivity", "Loading HomeFragment");
            loadFragment(new HomeFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_home); // Actualizar el BottomNavigationView
        } else {
            // Cargar HomeFragment por defecto si no hay extra
            loadFragment(new HomeFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_home); // Actualizar el BottomNavigationView
        }

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

        // Configurar el FloatingActionButton
        binding.buttonFloating.setOnClickListener(v -> {
            Intent intent = new Intent(this, PostActivity.class);
            startActivityForResult(intent, 100); // 100 es un código de solicitud
        });



    }


    private void loadInitialFragment() {
        String fragmentToLoad = getIntent().getStringExtra("FRAGMENT_TO_LOAD");
        if ("PROFILE".equals(fragmentToLoad)) {
            loadFragment(new PerfilFragment());
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_perfil);
        } else {
            loadFragment(new HomeFragment()); // Carga `HomeFragment` por defecto
            binding.bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("POST_PUBLICADO", false)) {
                Log.d("HomeActivity", "Post publicado, recargando HomeFragment");
                loadFragment(new HomeFragment()); // 🔹 Recargar HomeFragment
            }
        }
    }




}