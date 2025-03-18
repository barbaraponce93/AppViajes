package com.example.movilproyectofinal.viewModel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.movilproyectofinal.model.Post;
import com.example.movilproyectofinal.providers.PostProvider;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostViewModel extends ViewModel {
    private final MutableLiveData<Boolean> postSuccess = new MutableLiveData<>();
    private final PostProvider postProvider;
    private LiveData<List<Post>> posts;

    public PostViewModel() {
        postProvider = new PostProvider();
        posts = new MutableLiveData<>();
    }

    public LiveData<Boolean> getPostSuccess() {
        return postSuccess;
    }

    public LiveData<List<Post>> getPosts(int page) {
        return postProvider.getAllPosts(page); // Pasa la página actual a PostProvider
    }


    public void publicar(Post post) {
        postProvider.addPost(post).observeForever(result -> {
            if ("Post publicado".equals(result)) {
                postSuccess.setValue(true);
            } else {
                postSuccess.setValue(false);
            }
        });
    }


    public LiveData<List<Post>> getPostsByUser(ParseUser user) {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        postProvider.getAllPostsByUser(user, new PostProvider.PostsCallback() {
            @Override
            public void onSuccess(List<Post> posts) {
                result.setValue(posts);
            }

            @Override
            public void onFailure(ParseException e) {
                // Manejar el error
                result.setValue(null);
            }
        });
        return result;
    }



}
