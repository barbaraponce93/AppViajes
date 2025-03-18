package com.example.movilproyectofinal.view.fragments;

import static com.example.movilproyectofinal.adapters.PostAdapter.VIEW_TYPE_GRID;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;

import com.example.movilproyectofinal.adapters.PostAdapter;
import com.example.movilproyectofinal.model.Post;
import com.example.movilproyectofinal.util.ImageUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movilproyectofinal.R;
import com.example.movilproyectofinal.databinding.FragmentPerfilBinding;
import com.example.movilproyectofinal.view.Login;
import com.example.movilproyectofinal.view.PostDetailActivity;
import com.example.movilproyectofinal.viewModel.PostViewModel;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PerfilFragment extends Fragment {


    private FragmentPerfilBinding binding;

    private ActivityResultLauncher<Intent> galleryLauncher; //inicia la galería de imágenes y Maneja la selección de imágenes desde la galería.
    private PostViewModel postViewModel;
    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;



    public PerfilFragment() {
        // Es obligatorio que los fragmentos tengan un constructor vacío para que Android pueda recrearlos en caso de cambios de configuración.
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPerfilBinding.inflate(inflater,container,false);
        recyclerViewPosts = binding.recyclerViewPerfil;


        // Configurar RecyclerView con GridLayoutManager (3 columnas)
        recyclerViewPosts.setLayoutManager(new GridLayoutManager(getContext(), 3));
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        cargarPostsDelUsuarioLogueado(); // Cargar los posts del usuario logueado


        setupMenu();//Configura el menú de opciones del fragmento.
        setupToolbar();//Configura la Toolbar del fragmento.
        displayUserInfo();
        fetchPostCount();
        setupGalleryLauncher();//Configura el ActivityResultLauncher para la galería de imágenes.
        setupProfileImageClick();

        View view = binding.getRoot();

        // Configurar el Toolbar correctamente
        Toolbar toolbar = view.findViewById(R.id.tools_filtro);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle("");



        return view;
    }


///-----------------------TOOLBAR------------------------------------
    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.perfil_menu, menu);//Se crea un menú desde el archivo perfil_menu.xml.
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.itemLogout) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Cerrar Sesión")
                            .setMessage("¿Estás seguro de que quieres cerrar sesión?")
                            .setPositiveButton("Sí", (dialog, which) -> {

                                Toast.makeText(requireContext(), "Cerrando sesión...", Toast.LENGTH_SHORT).show();
                                ParseUser.logOut();    //  cierre de sesión de parse

                                // Vuelvo al Login
                                Intent intent = new Intent(getActivity(), Login.class);
                                startActivity(intent);
                                getActivity().finish();
                            })
                            .setNegativeButton("No", (dialog, which) -> {
                                // El usuario canceló el cierre de sesión
                                dialog.dismiss();
                            })
                            .show();
                    return true;
                }
                return false;
            }

        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);//Garantiza que el MenuProvider solo existe mientras la vista del fragmento está activa.
    }

    private void setupToolbar() {
        Toolbar toolbar = binding.getRoot().findViewById(R.id.tools_filtro);
        if (toolbar != null) {
  //Esto le dice a la actividad que use la Toolbar del fragmento como su ActionBar.(barra de herramientas que proporciona acciones y opciones de navegación)
            ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);
        } else {
            Log.e("PerfilFragment", "Error: Toolbar no encontrada en el layout.");
        }


    }
    ///-----------------------INFO USER------------------------------------

    private void displayUserInfo() {
        ParseUser currentUser = ParseUser.getCurrentUser();//OBTENGO EL USUARIO QUE INICIO SESION
        if (currentUser != null) {
            binding.nameUser.setText(currentUser.getUsername());
            binding.emailUser.setText(currentUser.getEmail());
            binding.insta.setText(currentUser.getString("instagram"));//ESTO FUNCINARIA SI HAY CAMPO CON ESTE NOMBRE EN LA BD

            String fotoUrl = currentUser.getString("foto_perfil");//OBTENGO LA FOTO DE PERFIL
            if (fotoUrl != null) {
                Picasso.get()//esta libreria cargar imágenes desde una URL.
                        .load(fotoUrl)//carga la imagen
                        .placeholder(R.drawable.ic_persona)//muestra el icono por defecto hasta que cargue la imagen
                        .error(R.drawable.ic_persona)//imagen alternativa si hay un error
                        .into(binding.circleImageView);// Asigna la imagen cargada al ImageView
            } else {
                binding.circleImageView.setImageResource(R.drawable.ic_persona);
            }
        }
    }

    ///-----------------------CONTADOR DE POST------------------------------------
    private void fetchPostCount() {
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser != null) {
            //crea una consulta que buscara objetos en la tabla "Post"
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
            query.whereEqualTo("user", currentUser);//filtra para buscar los post del usuario logueado
            query.countInBackground((count, e) -> {//cuenta el número de objetos que coinciden con la consulta.
                if (e == null) {
                    binding.conteoPublicaciones.setText(String.valueOf(count));//count es int por eso lo paso a string necesario para el setText
                } else {
                    binding.conteoPublicaciones.setText("0");
                }
            });
        } else {
            binding.conteoPublicaciones.setText("0");
        }
    }



    private void setupGalleryLauncher() {//abre la galeria y recibe el resultado
        galleryLauncher = registerForActivityResult(//registra un "lanzador de actividad" que se encargará de abrir la galería y manejar la imagen seleccionada.
                new ActivityResultContracts.StartActivityForResult(),
                result -> {//Este callback se ejecuta cuando la galería de imágenes finaliza y devuelve un resultado
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();//obtiene la URI obtiene la URI (identificador de la imagen en el almacenamiento del dispositivo).
                        if (imageUri != null) {//esta validacion se hace por si ocurre un problema con la galeria.
                            handleImageSelection(imageUri);//se encarga de procesar y mostrar la imagen en la interfaz.
                        }
                    }
                }
        );
    }

    private void setupProfileImageClick() {
        if (isAdded() && getContext() != null) {
            binding.circleImageView.setOnClickListener(v -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    ImageUtils.pedirPermisos(requireActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                ImageUtils.openGallery(requireContext(), galleryLauncher);
            });
        }
    }

    private void handleImageSelection(Uri imageUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(
                    requireActivity().getContentResolver(), imageUri);
            binding.circleImageView.setImageBitmap(bitmap);

            ImageUtils.subirImagenAParse(requireContext(), imageUri, new ImageUtils.ImageUploadCallback() {
                @Override
                public void onSuccess(String imageUrl) {
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    if (currentUser != null) {
                        currentUser.put("foto_perfil", imageUrl);
                        currentUser.saveInBackground(e -> {
                            if (e == null) {
                                Toast.makeText(requireContext(), "Foto subida correctamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Error al guardar la URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(requireContext(), "Error al subir la foto: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            Log.e("PerfilFragment", "Error al manejar la imagen: " + e.getMessage(), e);
            Toast.makeText(requireContext(), "Error al cargar la imagen", Toast.LENGTH_SHORT).show();
        }
    }


    private void cargarPostsDelUsuarioLogueado() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            postViewModel.getPostsByUser(currentUser).observe(getViewLifecycleOwner(), posts -> {
                if (posts != null) {
                    postAdapter = new PostAdapter(posts, (post, imageIndex) -> {

                        Log.d("PerfilFragment", "Post clickeado: " + post.getTitulo());
                        Intent intent = new Intent(getActivity(), PostDetailActivity.class);
                        startActivity(intent);
                    }, PostAdapter.VIEW_TYPE_GRID); // Aquí se pasa el tipo de vista
                    recyclerViewPosts.setAdapter(postAdapter);
                } else {
                    // Manejar el error
                }
            });
        }
    }



}