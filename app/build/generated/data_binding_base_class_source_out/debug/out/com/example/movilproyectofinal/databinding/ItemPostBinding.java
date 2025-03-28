// Generated by view binder compiler. Do not edit!
package com.example.movilproyectofinal.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.movilproyectofinal.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ItemPostBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final ImageView ivImage1;

  @NonNull
  public final ImageView ivImage2;

  @NonNull
  public final ImageView ivImage3;

  @NonNull
  public final TextView tvDescripcion;

  @NonNull
  public final TextView tvTitulo;

  private ItemPostBinding(@NonNull CardView rootView, @NonNull ImageView ivImage1,
      @NonNull ImageView ivImage2, @NonNull ImageView ivImage3, @NonNull TextView tvDescripcion,
      @NonNull TextView tvTitulo) {
    this.rootView = rootView;
    this.ivImage1 = ivImage1;
    this.ivImage2 = ivImage2;
    this.ivImage3 = ivImage3;
    this.tvDescripcion = tvDescripcion;
    this.tvTitulo = tvTitulo;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static ItemPostBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ItemPostBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.item_post, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ItemPostBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.ivImage1;
      ImageView ivImage1 = ViewBindings.findChildViewById(rootView, id);
      if (ivImage1 == null) {
        break missingId;
      }

      id = R.id.ivImage2;
      ImageView ivImage2 = ViewBindings.findChildViewById(rootView, id);
      if (ivImage2 == null) {
        break missingId;
      }

      id = R.id.ivImage3;
      ImageView ivImage3 = ViewBindings.findChildViewById(rootView, id);
      if (ivImage3 == null) {
        break missingId;
      }

      id = R.id.tvDescripcion;
      TextView tvDescripcion = ViewBindings.findChildViewById(rootView, id);
      if (tvDescripcion == null) {
        break missingId;
      }

      id = R.id.tvTitulo;
      TextView tvTitulo = ViewBindings.findChildViewById(rootView, id);
      if (tvTitulo == null) {
        break missingId;
      }

      return new ItemPostBinding((CardView) rootView, ivImage1, ivImage2, ivImage3, tvDescripcion,
          tvTitulo);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
