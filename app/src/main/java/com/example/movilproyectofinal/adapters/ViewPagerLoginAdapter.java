package com.example.movilproyectofinal.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.movilproyectofinal.view.fragments.InicioSesionFragment;
import com.example.movilproyectofinal.view.fragments.LogueoTabFragment;

public class ViewPagerLoginAdapter extends FragmentStateAdapter {



public ViewPagerLoginAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
    super(fragmentManager, lifecycle);
}
@NonNull
@Override
public Fragment createFragment(int position) {
    if (position == 1){
        return new LogueoTabFragment();// Cuando el tab es el segundo (posición 1), muestra LogueoTabFragment
    }
    return new InicioSesionFragment();
}
@Override
public int getItemCount() {
    return 2;
}
}
