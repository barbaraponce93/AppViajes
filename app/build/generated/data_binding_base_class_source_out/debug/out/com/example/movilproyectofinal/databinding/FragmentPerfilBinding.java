// Generated by view binder compiler. Do not edit!
package com.example.movilproyectofinal.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.movilproyectofinal.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentPerfilBinding implements ViewBinding {
  @NonNull
  private final CoordinatorLayout rootView;

  @NonNull
  public final AppBarLayout appBarLayout2;

  @NonNull
  public final CircleImageView circleImageView;

  @NonNull
  public final TextView conteoPublicaciones;

  @NonNull
  public final TextView emailUser;

  @NonNull
  public final TextView insta;

  @NonNull
  public final TextView nameUser;

  @NonNull
  public final RecyclerView recyclerViewPerfil;

  @NonNull
  public final TextView titlePosts;

  @NonNull
  public final MaterialToolbar toolsFiltro;

  private FragmentPerfilBinding(@NonNull CoordinatorLayout rootView,
      @NonNull AppBarLayout appBarLayout2, @NonNull CircleImageView circleImageView,
      @NonNull TextView conteoPublicaciones, @NonNull TextView emailUser, @NonNull TextView insta,
      @NonNull TextView nameUser, @NonNull RecyclerView recyclerViewPerfil,
      @NonNull TextView titlePosts, @NonNull MaterialToolbar toolsFiltro) {
    this.rootView = rootView;
    this.appBarLayout2 = appBarLayout2;
    this.circleImageView = circleImageView;
    this.conteoPublicaciones = conteoPublicaciones;
    this.emailUser = emailUser;
    this.insta = insta;
    this.nameUser = nameUser;
    this.recyclerViewPerfil = recyclerViewPerfil;
    this.titlePosts = titlePosts;
    this.toolsFiltro = toolsFiltro;
  }

  @Override
  @NonNull
  public CoordinatorLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentPerfilBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentPerfilBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_perfil, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentPerfilBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.appBarLayout2;
      AppBarLayout appBarLayout2 = ViewBindings.findChildViewById(rootView, id);
      if (appBarLayout2 == null) {
        break missingId;
      }

      id = R.id.circleImageView;
      CircleImageView circleImageView = ViewBindings.findChildViewById(rootView, id);
      if (circleImageView == null) {
        break missingId;
      }

      id = R.id.conteoPublicaciones;
      TextView conteoPublicaciones = ViewBindings.findChildViewById(rootView, id);
      if (conteoPublicaciones == null) {
        break missingId;
      }

      id = R.id.email_user;
      TextView emailUser = ViewBindings.findChildViewById(rootView, id);
      if (emailUser == null) {
        break missingId;
      }

      id = R.id.insta;
      TextView insta = ViewBindings.findChildViewById(rootView, id);
      if (insta == null) {
        break missingId;
      }

      id = R.id.name_user;
      TextView nameUser = ViewBindings.findChildViewById(rootView, id);
      if (nameUser == null) {
        break missingId;
      }

      id = R.id.recyclerViewPerfil;
      RecyclerView recyclerViewPerfil = ViewBindings.findChildViewById(rootView, id);
      if (recyclerViewPerfil == null) {
        break missingId;
      }

      id = R.id.title_posts;
      TextView titlePosts = ViewBindings.findChildViewById(rootView, id);
      if (titlePosts == null) {
        break missingId;
      }

      id = R.id.tools_filtro;
      MaterialToolbar toolsFiltro = ViewBindings.findChildViewById(rootView, id);
      if (toolsFiltro == null) {
        break missingId;
      }

      return new FragmentPerfilBinding((CoordinatorLayout) rootView, appBarLayout2, circleImageView,
          conteoPublicaciones, emailUser, insta, nameUser, recyclerViewPerfil, titlePosts,
          toolsFiltro);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
