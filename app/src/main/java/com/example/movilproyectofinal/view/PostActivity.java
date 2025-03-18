package com.example.movilproyectofinal.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.movilproyectofinal.R;
import com.example.movilproyectofinal.adapters.ImageAdapter;
import com.example.movilproyectofinal.databinding.ActivityPostBinding;
import com.example.movilproyectofinal.model.Post;
import com.example.movilproyectofinal.util.ImageUtils;
import com.example.movilproyectofinal.util.Validaciones;
import com.example.movilproyectofinal.viewModel.PostViewModel;

import java.util.ArrayList;
import java.util.List;




public class PostActivity extends AppCompatActivity {
    private static final int MAX_IMAGES = 3;
    private final int REQUEST_IMAGE = 1;
    private ActivityPostBinding binding;

    private PostViewModel postViewModel;
    private final List<String> imagenesUrls = new ArrayList<>();
    private ImageAdapter adapter;
    private String categoria;

    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupRecyclerView();
        setupViewModels();
        setupCategorySpinner();
        setupGalleryLauncher();
        binding.btnPublicar.setOnClickListener(v -> publicarPost());

        Toolbar toolbar = findViewById(R.id.tools);
        toolbar.setNavigationOnClickListener(v -> {
            // Volver a la actividad principal
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });





    }

    private void setupRecyclerView() {
        adapter = new ImageAdapter(imagenesUrls, this);
        binding.recyclerViewPost.setLayoutManager(new GridLayoutManager(this, 3));
        binding.recyclerViewPost.setAdapter(adapter);
        updateRecyclerViewVisibility();
    }

    private void setupViewModels() {
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);
        postViewModel.getPostSuccess().observe(this, exito -> {
            String mensaje = exito ? "Post publicado con éxito" : "Hubo un error al publicar el post";
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void setupCategorySpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, R.layout.spinner_item, getResources().getStringArray(R.array.categorias_array)
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategoria.setAdapter(adapter);
        binding.spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoria = null;
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void setupGalleryLauncher() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        if (imageUri != null && imagenesUrls.size() < MAX_IMAGES) {
                            ImageUtils.subirImagenAParse(PostActivity.this, imageUri, new ImageUtils.ImageUploadCallback() {
                                @Override
                                public void onSuccess(String imageUrl) {
                                    Log.d("PostActivity", "Imagen subida con éxito: " + imageUrl);
                                    imagenesUrls.add(imageUrl);
                                    adapter.notifyDataSetChanged();
                                    updateRecyclerViewVisibility();

                                }
                                @Override
                                public void onFailure(Exception e) {
                                    Log.e("PostActivity", "Error al subir la imagen", e);
                                    Toast.makeText(PostActivity.this, "Error al subir la imagen", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else if (imagenesUrls.size() >= MAX_IMAGES) {
                            Toast.makeText(PostActivity.this, "Máximo de imágenes alcanzado", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        binding.uploadImage.setOnClickListener(v -> ImageUtils.pedirPermisos(PostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE));
    }

    private void publicarPost() {
        String titulo = binding.itTitulo.getText().toString().trim();
        String descripcion = binding.etDescripcion.getText().toString().trim();
        String duracionStr = binding.etDuracion.getText().toString().trim();
        String presupuestoStr = binding.etPresupuesto.getText().toString().trim();


        if (!Validaciones.validarTexto(titulo)) {
            binding.itTitulo.setError("El título no es válido");
            return;
        }
        if (!Validaciones.validarTexto(descripcion)) {
            binding.etDescripcion.setError("La descripción no es válida");
            return;
        }
        int duracion = Validaciones.validarNumero(duracionStr);
        if (duracion == -1) {
            binding.etDuracion.setError("Duración no válida");
            return;
        }
        double presupuesto;
        try {
            presupuesto = Double.parseDouble(presupuestoStr);
        } catch (NumberFormatException e) {
            binding.etPresupuesto.setError("Presupuesto no válido");
            return;
        }
        Post post = new Post();
        post.setTitulo(titulo);
        post.setDescripcion(descripcion);
        post.setDuracion(duracion);
        post.setCategoria(categoria);
        post.setPresupuesto(presupuesto);
        post.setImagenes(new ArrayList<>(imagenesUrls));
        postViewModel.publicar(post);
    }

    public void updateRecyclerViewVisibility() {
        boolean hayImages = !imagenesUrls.isEmpty();
        binding.recyclerViewPost.setVisibility(hayImages ? View.VISIBLE : View.GONE);
        binding.uploadImage.setVisibility(imagenesUrls.size() < MAX_IMAGES ? View.VISIBLE : View.GONE);
        binding.frameImages.setVisibility(imagenesUrls.size() < MAX_IMAGES ? View.VISIBLE : View.GONE);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) binding.appBarLayout.getLayoutParams();


        if (imagenesUrls.size() >= MAX_IMAGES) {
            // Ajustar la altura del CollapsingToolbarLayout a WRAP_CONTENT si hay 3 imágenes
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else {
            // Restablecer la altura a 250dp si hay menos de 3 imágenes
            params.height = (int) getResources().getDimension(R.dimen.collapsing_toolbar_height); // Usar un recurso de dimensión
        }

        binding.appBarLayout.setLayoutParams(params);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("PostActivity", "onRequestPermissionsResult ejecutado");
        if (requestCode == REQUEST_IMAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d("PostActivity", "Permiso concedido, abriendo galería");
            ImageUtils.openGallery(PostActivity.this, galleryLauncher);
        } else {
            Log.d("PostActivity", "Permiso denegado");
            Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
        }
    }
}
