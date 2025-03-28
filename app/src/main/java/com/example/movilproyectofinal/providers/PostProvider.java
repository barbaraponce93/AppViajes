package com.example.movilproyectofinal.providers;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.movilproyectofinal.model.Post;
import com.example.movilproyectofinal.model.User;
import com.parse.GetCallback;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;



public class PostProvider {

    // Método para agregar un post
    public LiveData<String> addPost(Post post) {
        MutableLiveData<String> result = new MutableLiveData<>();

        if (post.getImagenes() == null || post.getImagenes().isEmpty()) {
            result.setValue("Error: El post debe tener al menos una imagen.");
            return result;
        }

        ParseObject postObject = ParseObject.create("Post");

        postObject.put("titulo", post.getTitulo());
        postObject.put("descripcion", post.getDescripcion());
        postObject.put("duracion", post.getDuracion());
        postObject.put("categoria", post.getCategoria());
        postObject.put("presupuesto", post.getPresupuesto());
        postObject.put("user", ParseUser.getCurrentUser()); // Relación con el usuario

        List<String> imagesToSave = post.getImagenes();
        AtomicInteger imagesSavedCount = new AtomicInteger(0);

        postObject.saveInBackground(e -> {
            if (e != null) {
                result.setValue("Error al guardar el post: " + (e.getMessage() != null ? e.getMessage() : "Error desconocido"));
            } else {
                ParseRelation<ParseObject> relation = postObject.getRelation("images");
                for (String url : imagesToSave) {
                    ParseObject imageObject = ParseObject.create("Image");
                    imageObject.put("url", url);
                    imageObject.saveInBackground(imgSaveError -> {
                        if (imgSaveError == null) {
                            relation.add(imageObject);
                            imagesSavedCount.incrementAndGet();
                            if(imagesSavedCount.get() == imagesToSave.size()) {
                                postObject.saveInBackground(saveError -> {
                                    if (saveError == null) {
                                        result.setValue("Post publicado");
                                    } else {
                                        result.setValue("Error al guardar la relación con las imágenes: " + (saveError.getMessage() != null ? saveError.getMessage() : "Error desconocido"));
                                    }
                                });
                            }
                        } else {
                            result.setValue("Error al guardar la imagen: " + (imgSaveError.getMessage() != null ? imgSaveError.getMessage() : "Error desconocido"));
                        }
                    });
                }
            }
        });
        return result;
    }

    // Método para obtener todos los posts, con paginación
    public LiveData<List<Post>> getAllPosts(int page) {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.include("user"); // Incluye información del usuario
        query.orderByDescending("createdAt");
        query.setLimit(5); // Límite de 5 posts por página
        query.setSkip(page * 5); // Desplaza según la página (5 posts por página)

        query.findInBackground((posts, e) -> {
            if (e == null) {
                List<Post> postList = new ArrayList<>();
                for (ParseObject postObject : posts) {
                    try {
                        // Crear un objeto Post de ParseObject
                        Post post = (Post) postObject;  // Usar el casteo a Post

                        // Verificar si los valores son nulos y asignar valores predeterminados si es necesario
                        String titulo = post.getTitulo();
                        String descripcion = post.getDescripcion();
                        int duracion = post.getDuracion();
                        String categoria = post.getCategoria();
                        double presupuesto = post.getPresupuesto();

                        // Si alguno de estos campos es nulo, asignar un valor predeterminado
                        if (titulo == null) titulo = "Título no disponible";
                        if (descripcion == null) descripcion = "Descripción no disponible";
                        if (categoria == null) categoria = "Categoría no disponible";

                        // Asignar los valores a los campos de post
                        post.setTitulo(titulo);
                        post.setDescripcion(descripcion);
                        post.setDuracion(duracion);
                        post.setCategoria(categoria);
                        post.setPresupuesto(presupuesto);

                        // Cargar imágenes
                        ParseRelation<ParseObject> relation = postObject.getRelation("images");
                        try {
                            List<ParseObject> images = relation.getQuery().find();
                            List<String> imageUrls = new ArrayList<>();
                            for (ParseObject imageObject : images) {
                                imageUrls.add(imageObject.getString("url"));
                            }
                            post.setImagenes(imageUrls);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }

                        postList.add(post);
                    } catch (Exception ex) {
                        Log.e("ParseError", "Error al procesar el post: ", ex);
                    }
                }
                result.setValue(postList);
            } else {
                result.setValue(new ArrayList<>());
                Log.e("ParseError", "Error al recuperar todos los posts: ", e);
            }
        });

        return result;
    }
    public interface PostsCallback {
        void onSuccess(List<Post> posts);
        void onFailure(ParseException e);
    }

    public void getAllPostsByUser(ParseUser user, PostsCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("user", user);  // Filtrar por el usuario
        query.include("user");  // Asegurar que se incluyan los datos del usuario
        query.include("images");  // Incluir las imágenes relacionadas
        query.orderByDescending("createdAt"); // Ordenar por fecha de creación descendente
        query.findInBackground((posts, e) -> {
            if (e == null) {
                List<Post> postList = new ArrayList<>();
                for (ParseObject postObject : posts) {
                    try {
                        // Crear un objeto Post a partir del ParseObject
                        Post post = (Post) postObject;  // Usar el casteo a Post

                        // Cargar imágenes
                        ParseRelation<ParseObject> relation = postObject.getRelation("images");
                        try {
                            List<ParseObject> images = relation.getQuery().find();
                            List<String> imageUrls = new ArrayList<>();
                            for (ParseObject imageObject : images) {
                                imageUrls.add(imageObject.getString("url"));
                            }
                            post.setImagenes(imageUrls);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }

                        postList.add(post);
                    } catch (Exception ex) {
                        Log.e("ParseError", "Error al procesar el post: ", ex);
                    }
                }
                callback.onSuccess(postList);  // Devolver los posts con las imágenes
            } else {
                callback.onFailure(e);  // Devolver error
            }
        });
    }


    public interface PostDetailCallback {
        void onSuccess(Post post);  // Llamado cuando el post se obtiene correctamente
        void onFailure(ParseException e);  // Llamado si ocurre un error
    }

    public interface CommentsCallback {
        void onSuccess(List<ParseObject> comments);
        void onFailure(Exception e);
    }

    public void getPostDetail(String postId, PostDetailCallback callback) {
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.getInBackground(postId, new GetCallback<Post>() {
            @Override
            public void done(Post post, ParseException e) {
                if (e == null) {
                    // Si la consulta fue exitosa, llamamos al método onSuccess del callback
                    callback.onSuccess(post);
                } else {
                    // Si ocurrió un error, llamamos al método onFailure del callback
                    callback.onFailure(e);
                }
            }
        });
    }
    public void fetchComments(String postId, CommentsCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comentario");
        query.whereEqualTo("post", ParseObject.createWithoutData("Post", postId));
        query.include("user"); // Incluye los datos del usuario en la consulta
        query.findInBackground((comentarios, e) -> {
            if (e == null) {
                callback.onSuccess(comentarios);
            } else {
                callback.onFailure(e);
            }
        });
    }

    public void saveComment(String postId, String commentText, ParseUser currentUser, SaveCallback callback) {
        // representa el post
        ParseObject post = ParseObject.createWithoutData("Post", postId);
        //representa el comentario
        ParseObject comentario = new ParseObject("Comentario");
        comentario.put("texto", commentText);//el texto del com
        comentario.put("post", post);        //referencia al post comentado
        comentario.put("user", currentUser);// usuario que comento
        comentario.saveInBackground(callback);
        // Aquí pasas el SaveCallback al método saveInBackground()
    }

    public LiveData<String> deletePost(String postId) {
        MutableLiveData<String> result = new MutableLiveData<>();

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.getInBackground(postId, (post, e) -> {
            if (e == null) {
                post.deleteInBackground(e1 -> {
                    if (e1 == null) {
                        Log.d("PostDelete", "Post eliminado con éxito.");
                        result.postValue("Post eliminado correctamente");
                    } else {
                        Log.e("PostDelete", "Error al eliminar el post: ", e1);
                        result.postValue("Error al eliminar el post: " + e1.getMessage());
                    }
                });
            } else {
                Log.e("PostDelete", "Error al encontrar el post: ", e);
                result.postValue("Error al encontrar el post: " + e.getMessage());
            }
        });

        return result;
    }

    public void buscarPostsPorTexto(String query, PostsCallback callback) {

        ParseQuery<ParseObject> queryTitulo = ParseQuery.getQuery("Post");

        queryTitulo.whereMatches("titulo", "(?i).*" + query + ".*"); // (?i) para ignorar mayúsculas y minúsculas


        ParseQuery<ParseObject> queryDescripcion = ParseQuery.getQuery("Post");

        queryDescripcion.whereMatches("descripcion", "(?i).*" + query + ".*");


        ParseQuery<ParseObject> queryCategoria = ParseQuery.getQuery("Post");

        queryCategoria.whereMatches("categoria", "(?i).*" + query + ".*");


        List<ParseQuery<ParseObject>> queries = new ArrayList<>();

        queries.add(queryTitulo);

        queries.add(queryDescripcion);

        queries.add(queryCategoria);


        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);

        mainQuery.include("user");

        mainQuery.include("images");


        mainQuery.findInBackground((posts, e) -> {

            if (e == null) {
                List<Post> postList = new ArrayList<>();
                for (ParseObject postObject : posts) {
                    try {
                            // Crear un objeto Post a partir del ParseObject
                        Post post = (Post) postObject;
                        // Cargar imágene
                        ParseRelation<ParseObject> relation = postObject.getRelation("images");

                        try {
                            List<ParseObject> images = relation.getQuery().find();
                            List<String> imageUrls = new ArrayList<>();
                            for (ParseObject imageObject : images) {
                                imageUrls.add(imageObject.getString("url"));
                            }
                            post.setImagenes(imageUrls);
                        } catch (ParseException parseException) {
                            parseException.printStackTrace();
                        }
                        postList.add(post);
                    } catch (Exception ex) {
                        Log.e("ParseError", "Error al procesar el post: ", ex);
                    }
                }
                callback.onSuccess(postList);
            } else {
                callback.onFailure(e);

            }

        });

    }






}

