package com.example.movilproyectofinal.adapters;



import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.example.movilproyectofinal.R;
import com.example.movilproyectofinal.model.Post;
import com.example.movilproyectofinal.model.User;
import com.example.movilproyectofinal.view.PostDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    public List<Post> posts;
    private OnPostClickListener listener; // Listener para manejar el click
    public static final int VIEW_TYPE_GRID = 1;
    public static final int VIEW_TYPE_LIST = 2;

    private int viewType;

    // Constructor con el listener
    public PostAdapter(List<Post> posts, OnPostClickListener listener, int viewType) {
        this.posts = posts != null ? posts : new ArrayList<>(); // Inicializar con lista vacía si es null
        this.listener = listener;
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_GRID) {
            // Infla el layout item_grid para la vista de perfil
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid, parent, false);
        } else {
            // Infla el layout item_post para la vista normal
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        }
        return new PostViewHolder(view,viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return viewType; // Retorna el tipo de vista asignado en el constructor
    }



    //aca cargo los detail, sea abierto por el perfil o el home.
    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);

        // Lógica para la vista (HomeFragment)
        if (viewType == VIEW_TYPE_LIST) {
            holder.tvTitulo.setText(post.getTitulo());
            holder.tvDescripcion.setText(post.getDescripcion());

            // Cargar imágenes para la vista de lista
            List<String> imagenes = post.getImagenes();
            if (imagenes != null && !imagenes.isEmpty()) {
                ImageView[] imageViews = {holder.ivImage1, holder.ivImage2, holder.ivImage3};

                for (ImageView imageView : imageViews) {
                    imageView.setVisibility(View.GONE);
                }

                for (int i = 0; i < imagenes.size(); i++) {
                    Picasso.get()
                            .load(imagenes.get(i))
                            .fit()
                            .centerCrop()
                            .error(R.drawable.upload5)
                            .into(imageViews[i]);
                    imageViews[i].setVisibility(View.VISIBLE);

                    final int index = i;


                    imageViews[i].setOnClickListener(v -> {
                        String postId = post.getId(); // Obtener el postId
                        Log.d("PostAdapter", "postId enviado: " + postId);

                        Intent intent = new Intent(holder.itemView.getContext(), PostDetailActivity.class);
                        intent.putExtra("idPost", postId); // Pasar el postId////
                        intent.putExtra("titulo", post.getTitulo());
                        intent.putExtra("descripcion", post.getDescripcion());
                        intent.putExtra("categoria", post.getCategoria());
                        intent.putExtra("duracion", post.getDuracion());
                        intent.putExtra("presupuesto", post.getPresupuesto());

                        //Datos del Usuario
                        User user = post.getUser();
                        if (user != null) {
                            Log.d("Postadapter", user.getUsername());
                            intent.putExtra("username", user.getUsername());
                            intent.putExtra("email", user.getEmail());
                            intent.putExtra("redsocial", user.getRedSocial());
                            intent.putExtra("foto_perfil", user.getString("foto_perfil"));
                        } else {
                            Log.d("Postadapter", "User is null");
                        }
                        // Lista de imágenes
                        ArrayList<String> imageUrls = new ArrayList<>(post.getImagenes());
                        intent.putStringArrayListExtra("imagenes", imageUrls);

                        intent.putExtra("SOURCE_ACTIVITY", "HOME"); // Agregar esto
                        holder.itemView.getContext().startActivity(intent);
                    });
                }
            }

        } else if (viewType == VIEW_TYPE_GRID) {
            // Lógica para la vista  (PerfilFragment)

            // Cargar la primera imagen
            List<String> imagenes = post.getImagenes();
            if (imagenes != null && !imagenes.isEmpty()) {
                Picasso.get()
                        .load(imagenes.get(0))
                        .fit()
                        .centerCrop()
                        .error(R.drawable.upload5)
                        .into(holder.gridImage); // Usar gridImage
                holder.gridImage.setVisibility(View.VISIBLE);

                holder.gridImage.setOnClickListener(v -> {
                    String postId = post.getId(); // Obtener el postId
                    Log.d("PostAdapter", "postId enviado GRID: " + postId); // Agregar este log

                    Intent intent = new Intent(holder.itemView.getContext(), PostDetailActivity.class);
                    intent.putExtra("idPost", postId); // Pasar el postId
                    intent.putExtra("titulo", post.getTitulo());
                    intent.putExtra("descripcion", post.getDescripcion());
                    intent.putExtra("categoria", post.getCategoria());
                    intent.putExtra("duracion", post.getDuracion());
                    intent.putExtra("presupuesto", post.getPresupuesto());
                    Log.d("PostAdapter", "user esta ahi?: " + post.getUser().getUsername());


                   // Datos del Usuario
                    User user = post.getUser();
                    if (user != null) {
                        intent.putExtra("username", user.getUsername());
                        intent.putExtra("email", user.getEmail());
                        intent.putExtra("redsocial", user.getRedSocial());
                        intent.putExtra("foto_perfil", user.getString("foto_perfil"));
                    } else {
                        Log.d("Postadapter", "User is null");
                    }

                    // Lista de imágenes
                    ArrayList<String> imageUrls = new ArrayList<>(post.getImagenes());
                    intent.putStringArrayListExtra("imagenes", imageUrls);

                    intent.putExtra("SOURCE_ACTIVITY", "PROFILE");//indican la Activity de origen. lo uso para el retroceso
                    holder.itemView.getContext().startActivity(intent);
                });
            }

            // Mostrar el título
            holder.gridSubtitulo.setText(post.getTitulo()); // Mostrar el título
        }


    }


    @Override
    public int getItemCount() {
        return posts.size();
    }


    // ViewHolder para manejar las vistas
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion, gridSubtitulo;
        ImageView ivImage1, ivImage2, ivImage3, gridImage;

        public PostViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == VIEW_TYPE_LIST) {
                tvTitulo = itemView.findViewById(R.id.tvTitulo);
                tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
                ivImage1 = itemView.findViewById(R.id.ivImage1);
                ivImage2 = itemView.findViewById(R.id.ivImage2);
                ivImage3 = itemView.findViewById(R.id.ivImage3);
            } else if (viewType == VIEW_TYPE_GRID) {
                gridImage = itemView.findViewById(R.id.gridImage);
                gridSubtitulo = itemView.findViewById(R.id.gridSubtitulo);
            }
        }
    }

    // Interface para manejar el click en el post
    public interface OnPostClickListener {
        void onPostClick(Post post, int imageIndex);
    }
}