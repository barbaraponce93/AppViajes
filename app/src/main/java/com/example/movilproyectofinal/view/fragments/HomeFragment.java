package com.example.movilproyectofinal.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movilproyectofinal.adapters.PostAdapter;
import com.example.movilproyectofinal.databinding.FragmentHomeBinding;
import com.example.movilproyectofinal.model.Post;
import com.example.movilproyectofinal.view.HomeActivity;
import com.example.movilproyectofinal.view.PostDetailActivity;
import com.example.movilproyectofinal.viewModel.PostViewModel;

import java.util.ArrayList;
import java.util.List;


    public class HomeFragment extends Fragment implements PostAdapter.OnPostClickListener { // Implementa el listener aquí

        private FragmentHomeBinding binding;
        private PostViewModel postViewModel;
        private List<Post> postList;
        private PostAdapter adapter;
        private int currentPage = 0; // Página actual
        private boolean isLoading = false; // Para evitar cargas múltiples

        public static final int VIEW_TYPE_LIST = 2;

        public HomeFragment() {}

        public static HomeFragment newInstance(String p1, String p2) {
            return new HomeFragment();
        }

        @Override
        public void onResume() {
            super.onResume();
            cargarPosts(); //recarga los posts
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            binding = FragmentHomeBinding.inflate(inflater, container, false);
            return binding.getRoot();
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);


            postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

            postList = new ArrayList<>();
            adapter = new PostAdapter(postList, this, VIEW_TYPE_LIST); // Pasamos VIEW_TYPE_LIST
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.recyclerView.setAdapter(adapter);

            cargarPosts();

            // Listener para cargar más posts al hacer scroll
            binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    // Comprueba si el usuario ha llegado al final
                    LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int totalItemCount = layoutManager.getItemCount();
                        int visibleItemCount = layoutManager.getChildCount();
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                        // Si estamos en el final de la lista
                        if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
                            cargarPosts(); // Cargar más posts
                        }
                    }
                }
            });


        }


        private void cargarPosts() {
            if (isLoading) return; // Evita solicitudes múltiples mientras se está cargando
            isLoading = true; // Marca como cargando
            postViewModel.getPosts(currentPage).observe(getViewLifecycleOwner(), posts -> {
                if (posts != null && !posts.isEmpty()) {
                    postList.addAll(posts); // Añade nuevos posts a la lista existente
                    adapter.notifyDataSetChanged(); // Notifica al adaptador que los datos han cambiado
                    currentPage++; // Incrementa la página después de cargar los posts
                }
                isLoading = false; // Permite nuevas solicitudes
                // 🔹 Oculta el ProgressBar una vez que los posts están cargados
                if (getActivity() instanceof HomeActivity) {
                    ((HomeActivity) getActivity()).hideProgressBar();
                }
            });
        }

        @Override
        public void onPostClick(Post post, int imageIndex) {
            Intent intent = new Intent(getActivity(), PostDetailActivity.class);
            startActivity(intent);

        }


        @Override
        public void onDestroyView() {
            super.onDestroyView();

            binding = null;
        }




    }
