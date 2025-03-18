package com.example.movilproyectofinal.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movilproyectofinal.R;

import java.util.List;

//maneja una lista de imágenes.
class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.CarouselViewHolder> {

    private List<String> imageUrls;//lista de urls de imagenes que se van a mostrar en el carrusel

    public CarouselAdapter(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public CarouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {//Crea el diseño de cada item
        View view = LayoutInflater.from(parent.getContext())//Infla el diseño de cada imagen del carrusel
                .inflate(R.layout.item_slider_image, parent, false);
        return new CarouselViewHolder(view); //creando una instancia de carrusel pasando la vista inflada.
    }

    @Override
    public void onBindViewHolder(@NonNull CarouselViewHolder holder, int position) {//Carga la imagen en cada item,Se ejecuta cada vez que el RecyclerView necesita mostrar una imagen.
        String imageUrl = imageUrls.get(position);//Obtiene la URL de la imagen en la posición
        Glide.with(holder.imageView.getContext())
                .load(imageUrl)//carga
                .into(holder.imageView);//muestra
    }

    @Override
    public int getItemCount() {//Devuelve el número de imágenes
        return imageUrls.size();
    }

    public static class CarouselViewHolder extends RecyclerView.ViewHolder {//representar cada imagen en el carrusel.
        ImageView imageView; //donde se carga la imagen

        public CarouselViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);//referencia al ImageView donde se cargara la imagen
        }
    }
}
