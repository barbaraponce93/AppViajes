package com.example.movilproyectofinal.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movilproyectofinal.model.Post;
import com.example.movilproyectofinal.providers.PostProvider;
import com.parse.ParseException;

import java.util.List;

public class BusquedaViewModel extends ViewModel {
    private MutableLiveData<List<Post>> resultados = new MutableLiveData<>();
    private PostProvider postProvider = new PostProvider();

    public LiveData<List<Post>> getResultados() {
        return resultados;

    }

    public void buscarPosts(String query) {
        postProvider.buscarPostsPorTexto(query, new PostProvider.PostsCallback() {
            @Override
            public void onSuccess(List<Post> posts) {
                resultados.setValue(posts);
            }

            @Override
            public void onFailure(ParseException e) {
                resultados.setValue(null); // O maneja el error de otra manera
            }
        });
    }


}
