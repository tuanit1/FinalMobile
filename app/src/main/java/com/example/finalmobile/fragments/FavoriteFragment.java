package com.example.finalmobile.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.finalmobile.R;
import com.example.finalmobile.adapters.ViewPagerAdapter;
import com.example.finalmobile.databinding.FragmentFavoriteBinding;
import com.example.finalmobile.listeners.FavoriteToDetailListener;

public class FavoriteFragment extends Fragment {

    private ViewPagerAdapter viewPagerAdapter;
    private View rootView;
    private NavController navController;
    private FragmentFavoriteBinding binding;
    private FavoriteToDetailListener listener;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteBinding.inflate(inflater, container, false);
        rootView = binding.getRoot();
        
        return rootView;
    }
}