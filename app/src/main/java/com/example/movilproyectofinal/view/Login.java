package com.example.movilproyectofinal.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;



import com.example.movilproyectofinal.adapters.ViewPagerLoginAdapter;
import com.example.movilproyectofinal.databinding.ActivityLoginBinding;
import com.example.movilproyectofinal.providers.AuthProvider;
import com.example.movilproyectofinal.viewModel.LoginViewModel;
import com.google.android.material.tabs.TabLayout;



public class Login extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerLoginAdapter adapter;

    private ActivityLoginBinding binding;

    private LoginViewModel viewModel;
    private AuthProvider authProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this) .get(LoginViewModel.class);

       //Nota: Los id en snake_case se convierten a camelCase en el binding.
        tabLayout = binding.tabLayout;
        viewPager2 = binding.viewPager;

        // se crea el adapter que va a manejar los fragmentos que se mostraran en el viewPager2
        //FragmentManager maneja los fragmentos
        //getLifecycle() le dice al Adapter que siga el ciclo de vida de la actividad.
        FragmentManager fragmentManager=getSupportFragmentManager();
        adapter=new ViewPagerLoginAdapter(fragmentManager,getLifecycle());
        viewPager2.setAdapter(adapter);

        //agrego los titulos del tab
        tabLayout.addTab(tabLayout.newTab().setText("Iniciar sesión"));
        tabLayout.addTab(tabLayout.newTab().setText("Registrarse"));



        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //hace que se cambie el fragmento al tab que seleccionaste.
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //funcion al deseleccionar
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //reseleccion
            }



        });

            // Esto asegura que cuando deslices las páginas con el ViewPager2
        // // el TabLayout se actualice automáticamente, resaltando el tab correcto.
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.selectTab(tabLayout.getTabAt(position));

            }
        });


    }
    public void cambiarATabInicioSesion() {
        // 0 es el índice del InicioSesionFragment en el ViewPager
        viewPager2.setCurrentItem(0, true);
    }



}