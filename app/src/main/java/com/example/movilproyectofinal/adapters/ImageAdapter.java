package com.example.movilproyectofinal.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movilproyectofinal.R;
import com.example.movilproyectofinal.view.PostActivity;

import java.util.List;

//adaptador para un RecyclerView en Android, diseñado para mostrar una lista de imágenes a partir de URLs.
// Usa la biblioteca Glide para cargar las imágenes de manera eficiente.
//Se hereda recyclerView. imageViewHolder clase interna que representa cada imagen en la lista
public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<String> imageUrls;//Lista de URLs de imágenes que se van a mostrar en el RecyclerView
    private Context context;//Contexto de la actividad o fragmento, necesario para inflar layouts y cargar imágenes.

    public ImageAdapter(List<String> imageUrls, Context context) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Utilizar el atributo 'context' en lugar de 'parent.getContext()'
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
       // Infla  el diseño XML item_image.xml para cada elemento de la lista.
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        String imageUrl = imageUrls.get(position);
        Glide.with(context)
                .load(imageUrl)//carga la imagen
                .into(holder.imageView);//la muestra en el ImageView
        //Obtiene la URL de la imagen de la posición actual (position)



        holder.btnEliminar.setOnClickListener(v -> {
            int posicion = holder.getAdapterPosition();
            if (posicion != RecyclerView.NO_POSITION) {
                imageUrls.remove(posicion);
                notifyItemRemoved(posicion);
                // Notificar a la Activity para actualizar la UI si es necesario
                if (context instanceof PostActivity) {
                    ((PostActivity) context).updateRecyclerViewVisibility();
                }
            }
        });
    }

    @Override
    public int getItemCount() {//nos devuelve el numero total de elementos de la lista y asi el recycler sabe cuantos elementos mostrar
        return imageUrls.size();
    }


    public void updateImages(List<String> newImageUrls) {
        imageUrls = newImageUrls;//Actualiza la lista de imágenes con una nueva lista
        notifyDataSetChanged();//este metodo es para que el RecyclerView actualice la vista con las nuevas imágenes.
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {//permite almacenar y reutilizar las vistas
        ImageView imageView;
        ImageButton btnEliminar;
        public ImageViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView); // Asegúrate que esto coincida con tu layout
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}