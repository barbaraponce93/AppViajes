package com.example.movilproyectofinal.view;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.movilproyectofinal.R;
import com.example.movilproyectofinal.adapters.ComentarioAdapter;
import com.example.movilproyectofinal.adapters.EfectoTransformer;
import com.example.movilproyectofinal.adapters.ImageSliderAdapter;
import com.example.movilproyectofinal.databinding.ActivityPostDetailBinding;
import com.example.movilproyectofinal.view.fragments.PerfilFragment;
import com.example.movilproyectofinal.viewModel.PostDetailViewModel;
import com.google.android.material.tabs.TabLayoutMediator;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {
    private ActivityPostDetailBinding binding;
    private PostDetailViewModel postDetailViewModel;
    private ComentarioAdapter comentarioAdapter;
    private String postId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        postDetailViewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        detailInfo();
        setupObservers();
        postId = getIntent().getStringExtra("idPost");



        if (postId != null) {
            postDetailViewModel.fetchCommentario(postId);
        }

        binding.recyclerComentarios.setLayoutManager(new LinearLayoutManager(this));
        comentarioAdapter = new ComentarioAdapter(new ArrayList<>());
        binding.recyclerComentarios.setAdapter(comentarioAdapter);

        // Observing comments
        postDetailViewModel.getCommentsLiveData().observe(this, comentarios -> {
            comentarioAdapter.setComentarios(comentarios);
            comentarioAdapter.notifyDataSetChanged();
        });

        String currentUser = ParseUser.getCurrentUser().getUsername();
        String perfilUserId = getIntent().getStringExtra("username");

        if (currentUser != null && currentUser.equals(perfilUserId)) {
            binding.btnEliminarPost.setVisibility(View.VISIBLE);
            binding.btnEliminarPost.setOnClickListener(v -> confirmaBorrar());
        } else {
            binding.btnEliminarPost.setVisibility(View.GONE);
        }

        binding.fabComentar.setOnClickListener(v -> comentar());


        Toolbar toolbar = findViewById(R.id.toolsDetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Habilita el botón de retroceso

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            String sourceActivity = getIntent().getStringExtra("SOURCE_ACTIVITY");
            Intent intent;
            if ("PROFILE".equals(sourceActivity)) {
                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("FRAGMENT_TO_LOAD", "PROFILE");
            } else {
                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("FRAGMENT_TO_LOAD", "HOME");
            }
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void confirmaBorrar() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirmación");
        alert.setMessage("¿Estás seguro de que deseas eliminar este post?");

        alert.setPositiveButton("Eliminar", (dialog, which) -> postDetailViewModel.eliminarPost(postId));

        alert.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        alert.show();
    }

    private void comentar() {
        AlertDialog.Builder alert = new AlertDialog.Builder(PostDetailActivity.this);
        alert.setTitle("¡COMENTARIO!");
        alert.setMessage("Ingresa tu comentario: ");

        EditText editText = new EditText(PostDetailActivity.this);
        editText.setHint("Texto");
        //crea dinamicamente
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        editText.setLayoutParams(params);
        params.setMargins(36, 0, 36, 36);

        RelativeLayout container = new RelativeLayout(PostDetailActivity.this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(relativeParams);
        container.addView(editText);
        alert.setView(container);

        alert.setPositiveButton("Ok", (dialog, which) -> {
            String value = editText.getText().toString().trim();
            if (!value.isEmpty()) {
                postDetailViewModel.grabaComentario(postId, value);////////////////////////////
            } else {
                Toast.makeText(PostDetailActivity.this, "El comentario no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());//dialog.dismiss() cierra el cuadro de diálogo.

        alert.show();
    }//esete metodo nos va a mostrar el cuadro de dialogo por el que el usuario podra comentar.

    private void setupObservers() {
        postDetailViewModel.getCommentsLiveData().observe(this, comments -> {
            // updateUI(comments);
        });

        postDetailViewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_SHORT).show();
            }
        });

        postDetailViewModel.getSuccessLiveData().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish(); // Cierra la actividad tras eliminar el post
        });
    }

    private void detailInfo() {// recibo la info del adapter para mostrar en el detail
        binding.nameUser.setText(getIntent().getStringExtra("username"));
        binding.emailUser.setText(getIntent().getStringExtra("email"));

        String fotoUrl = getIntent().getStringExtra("foto_perfil");
        if (fotoUrl != null) {
            Picasso.get()
                    .load(fotoUrl)
                    .placeholder(R.drawable.ic_persona)
                    .error(R.drawable.ic_persona)
                    .into(binding.circleImageView);
        } else {
            binding.circleImageView.setImageResource(R.drawable.ic_persona);
        }

        ArrayList<String> urls = getIntent().getStringArrayListExtra("imagenes");
        String titulo = "Lugar: " + getIntent().getStringExtra("titulo");
        binding.lugar.setText(titulo);
        String categoria = "Categoria: " + getIntent().getStringExtra("categoria");
        binding.categoria.setText(categoria);
        String comentario = "descripción: " + getIntent().getStringExtra("descripcion");
        binding.description.setText(comentario);
        String duracion = "Duración: " + getIntent().getIntExtra("duracion", 0) + " día/s";
        binding.duracion.setText(duracion);
        String presupuesto = "Presupuesto: U$ " + getIntent().getDoubleExtra("presupuesto", 0.0);
        binding.presupuesto.setText(presupuesto);

        if (urls != null && !urls.isEmpty()) {
            ImageSliderAdapter imageSliderAdapter = new ImageSliderAdapter(urls);
            binding.viewPager.setAdapter(imageSliderAdapter);
            binding.viewPager.setPageTransformer(new EfectoTransformer());

            // Conexión TabLayout con ViewPager2
            new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            }).attach();
        }
    }
}
