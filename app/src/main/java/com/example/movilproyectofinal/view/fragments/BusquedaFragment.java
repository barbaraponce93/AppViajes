package com.example.movilproyectofinal.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.movilproyectofinal.R;
import com.example.movilproyectofinal.adapters.PostAdapter;
import com.example.movilproyectofinal.model.Post;
import com.example.movilproyectofinal.providers.PostProvider;
import com.example.movilproyectofinal.view.HomeActivity;
import com.example.movilproyectofinal.viewModel.BusquedaViewModel;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;


public class BusquedaFragment extends Fragment {


    private EditText etBusqueda;
    private Button botonBusqueda;
    private EditText etNoResultados;
    private RecyclerView recyclerViewFiltros;
    private PostAdapter adapter;
    private PostProvider postProvider;

    private BusquedaViewModel viewModel;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busqueda, container, false);



        etBusqueda = view.findViewById(R.id.etBusqueda);
        botonBusqueda = view.findViewById(R.id.botonBusqueda);
        etNoResultados = view.findViewById(R.id.etNoResultados);
        recyclerViewFiltros = view.findViewById(R.id.recyclerViewFiltros);

        postProvider = new PostProvider();
        // Inicializar el Adapter con una lista vacía y el tipo de vista LIST
        adapter = new PostAdapter(null, null, PostAdapter.VIEW_TYPE_LIST);
        BusquedaViewModel viewModel = new ViewModelProvider(this).get(BusquedaViewModel.class);

        // Configurar el LayoutManager y el Adapter en el RecyclerView
        recyclerViewFiltros.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewFiltros.setAdapter(adapter);

        // Observar los resultados del ViewModel
        viewModel.getResultados().observe(getViewLifecycleOwner(), posts -> {
            if (posts != null && !posts.isEmpty()) {
                adapter.posts = posts;
                adapter.notifyDataSetChanged();
                etNoResultados.setVisibility(View.GONE);
            } else {
                adapter.posts = new ArrayList<>();
                adapter.notifyDataSetChanged();
                etNoResultados.setVisibility(View.VISIBLE);
            }
        });

        botonBusqueda.setOnClickListener(v -> realizarBusqueda());



        return view;
    }

    private void realizarBusqueda() {

        String query = etBusqueda.getText().toString().trim();
        if (!query.isEmpty()) {
            postProvider.buscarPostsPorTexto(query, new PostProvider.PostsCallback() {
                @Override
                public void onSuccess(List<Post> posts) {
                    if (posts != null && !posts.isEmpty()) {
                        adapter.posts = posts;
                        adapter.notifyDataSetChanged();
                        etNoResultados.setVisibility(View.GONE); // Ocultar EditText
                    } else {
                        // Manejar el caso de lista vacía
                        Log.d("BusquedaFragment", "No se encontraron resultados.");
                        adapter.posts = new ArrayList<>(); // Limpiar la lista
                        adapter.notifyDataSetChanged();
                        etNoResultados.setVisibility(View.VISIBLE); // Mostrar EditText
                        // Mostrar mensaje o imagen de lista vacía
                    }
                }

                @Override
                public void onFailure(ParseException e) {
                    Log.e("BusquedaFragment", "Error en la búsqueda: " + e.getMessage());
                    adapter.posts = new ArrayList<>();
                    adapter.notifyDataSetChanged();
                    etNoResultados.setVisibility(View.VISIBLE); // Mostrar EditText en caso de error
                }
            });
        } else {
            adapter.posts = new ArrayList<>();
            adapter.notifyDataSetChanged();
            etNoResultados.setVisibility(View.GONE); // Ocultar EditText si la búsqueda está vacía


        }
    }



}